package cn.zx.cang.ai.user.domain.service;

import cn.hutool.crypto.digest.DigestUtil;
import cn.zx.cang.ai.api.user.constant.UserOperateTypeEnum;
import cn.zx.cang.ai.api.user.constant.UserStateEnum;
import cn.zx.cang.ai.api.user.request.UserActiveRequest;
import cn.zx.cang.ai.api.user.request.UserAuthRequest;
import cn.zx.cang.ai.api.user.request.UserModifyRequest;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.base.exception.BizException;
import cn.zx.cang.ai.base.exception.RepoErrorCode;
import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.lock.DistributeLock;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.domain.entity.UserOperateStream;
import cn.zx.cang.ai.user.domain.entity.convertor.UserConvertor;
import cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode;
import cn.zx.cang.ai.user.infrastructure.exception.UserException;
import cn.zx.cang.ai.user.infrastructure.mapper.UserMapper;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.zx.cang.ai.user.infrastructure.mapper.UserOperateStreamMapper;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.*;

/**
 * 用户服务
 *
 * @author kinchou
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements InitializingBean {

    private static final String DEFAULT_NICK_NAME_PREFIX = "藏家_";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOperateStreamService userOperateStreamService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedissonClient redissonClient;

    private RBloomFilter<String> nickNameBloomFilter;

    private RBloomFilter<String> inviteCodeBloomFilter;

    private RScoredSortedSet<String> inviteRank;

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, User> idUserCache;

    @Autowired
    private UserOperateStreamMapper userOperateStreamMapper;

    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":user:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        idUserCache = cacheManager.getOrCreateCache(idQc);
    }

    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse register(String telephone, String inviteCode) {
        // 默认昵称
        String defaultNickName;
        // 自己的邀请码
        String randomString;
        //生成邀请码、默认昵称
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + telephone.substring(7, 11);
        } while (nickNameExist(defaultNickName) && inviteCodeExist(inviteCode));

        //判断是否有邀请码
        String inviterId = null;
        if (StringUtils.isNotBlank(inviteCode)) {
            User inviter = userMapper.findByInviteCode(inviteCode);
            if (inviter != null) {
                //根据邀请码查询邀请人id
                inviterId = inviter.getId().toString();
            }
        }
        //注册
        User user = register(telephone, defaultNickName, telephone, randomString, inviterId);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        //将新生成的昵称和邀请码加入布隆过滤器
        addNickName(defaultNickName);
        addInviteCode(inviteCode);

        //更新排行榜
        updateInviteRank(inviterId);
        //更新当前用户信息
        updateUserCache(user.getId().toString(), user);
        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    /**
     * 更新用户信息
     * @param userModifyRequest
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userModifyRequest.userId")
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userModifyRequest.getUserId());
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        Assert.isTrue(user.canModifyInfo(), () -> new UserException(USER_STATUS_CANT_OPERATE));

        if (StringUtils.isNotBlank(userModifyRequest.getNickName()) && nickNameExist(userModifyRequest.getNickName())) {
            throw new UserException(NICK_NAME_EXIST);
        }
        BeanUtils.copyProperties(userModifyRequest, user);

        if (StringUtils.isNotBlank(userModifyRequest.getPassword())) {
            user.setPasswordHash(DigestUtil.md5Hex(userModifyRequest.getPassword()));
        }
        if (updateById(user)) {
            //加入流水
            long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.MODIFY);
            Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
            addNickName(userModifyRequest.getNickName());
            userOperatorResponse.setSuccess(true);

            return userOperatorResponse;
        }
        userOperatorResponse.setSuccess(false);
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
        userOperatorResponse.setResponseMessage(UserErrorCode.USER_OPERATE_FAILED.getMessage());

        return userOperatorResponse;
    }

    private void updateUserCache(String userId, User user) {
        idUserCache.put(userId, user);
    }

    private void updateInviteRank(String inviterId) {
        if (inviterId == null) {
            return;
        }
        RLock rLock = redissonClient.getLock(inviterId);
        rLock.lock();
        try {
            Double score = inviteRank.getScore(inviterId);
            if (score == null) {
                score = 0.0;
            }
            inviteRank.add(score + 100.0, inviterId);
        } finally {
            rLock.unlock();
        }
    }

    private boolean addInviteCode(String inviteCode) {
        return this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.add(inviteCode);
    }

    private boolean addNickName(String nickName) {
        return this.nickNameBloomFilter != null && this.nickNameBloomFilter.add(nickName);
    }

    /**
     * 注册用户
     * @param telephone
     * @param nickName
     * @param password
     * @param inviteCode
     * @param inviterId
     * @return
     */
    private User register(String telephone, String nickName, String password, String inviteCode, String inviterId) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }
        User user = new User();
        user.register(telephone, nickName, password, inviteCode, inviterId);
        return save(user) ? user : null;
    }

    private boolean inviteCodeExist(String inviteCode) {
        if (this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.contains(inviteCode)) {
            return userMapper.findByInviteCode(inviteCode) != null;
        }
        return false;
    }

    private boolean nickNameExist(String defaultNickName) {
        if (this.nickNameBloomFilter != null && this.nickNameBloomFilter.contains(defaultNickName)) {
            return userMapper.findByNickname(defaultNickName) != null;
        }
        return false;
    }

    /**
     * 管理员注册
     * @param telephone
     * @param password
     * @return
     */
    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    public UserOperatorResponse registerAdmin(String telephone, String password) {
        User user = registerAdmin(telephone, telephone, password);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());
        idUserCache.put(user.getId().toString(), user);

        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    private User registerAdmin(String telephone, String nickName, String password) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        User user = new User();
        user.registerAdmin(telephone, nickName, password);
        return save(user) ? user:null;
    }

    /**
     * 实名认证
     *
     * @param userAuthRequest
     * @return
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userAuthRequest.userId")
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse auth(UserAuthRequest userAuthRequest) {
        // 1. 判断用户是否存在
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.selectById(userAuthRequest.getUserId());
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        // 2. 判断用户是否已实名认证
        if(user.getState()==UserStateEnum.AUTH || user.getState() == UserStateEnum.ACTIVE){
            userOperatorResponse.setSuccess(Boolean.TRUE);
            userOperatorResponse.setUser(UserConvertor.INSTANCE.mapToVo(user));
            return userOperatorResponse;
        }
        Assert.isTrue(user.getState()==UserStateEnum.INIT, () -> new UserException(USER_STATUS_IS_NOT_INIT));
        // 3.1 进行实名认证
        boolean authResult = authService.checkAuth(userAuthRequest.getRealName(), userAuthRequest.getIdCard());
        Assert.isTrue(authResult, () -> new UserException(USER_AUTH_FAIL));
        // 3.2 用户状态更新
        user.auth(userAuthRequest.getRealName(),userAuthRequest.getIdCard());
        boolean result = updateById(user);
        if(!result){
            userOperatorResponse.setSuccess(false);
            userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
            userOperatorResponse.setResponseMessage(UserErrorCode.USER_OPERATE_FAILED.getMessage());
            return userOperatorResponse;
        }
        // 3.3 用户操作流水记录
        UserOperateStream userOperateStream = new UserOperateStream();
        Long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.AUTH);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
        userOperatorResponse.setSuccess(true);
        userOperatorResponse.setUser(UserConvertor.INSTANCE.mapToVo(user));
        return userOperatorResponse;
    }

    /**
     * 用户激活
     *
     * @param userActiveRequest
     * @return
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userActiveRequest.userId")
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse active(UserActiveRequest userActiveRequest) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userActiveRequest.getUserId());
        //判断用户是否存在
        Assert.notNull(user, ()-> new UserException(USER_NOT_EXIST));
        //判断用户是否已实名认证
        Assert.isTrue(user.getState()== UserStateEnum.AUTH,()->new UserException(USER_STATUS_IS_NOT_AUTH));
        //填充用户链平台与链接
        user.active(userActiveRequest.getBlockChainUrl(), userActiveRequest.getBlockChainPlatform());
        //更新数据库
        boolean result = updateById(user);
        if (result) {
            //加入流水
            long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.ACTIVE);
            Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
            userOperatorResponse.setSuccess(true);
        } else {
            userOperatorResponse.setSuccess(false);
            userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
            userOperatorResponse.setResponseMessage(UserErrorCode.USER_OPERATE_FAILED.getMessage());
        }
        return userOperatorResponse;
    }

    /**
     * 冻结
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse freeze(Long userId) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        //查询用户
        User user = userMapper.findById(userId);
        Assert.notNull(user, ()-> new UserException(USER_NOT_EXIST));
        Assert.isTrue(user.getState()==UserStateEnum.ACTIVE,()->new UserException(USER_STATUS_IS_NOT_ACTIVE));
        //第一次删除缓存
        idUserCache.remove(userId.toString());
        if (user.getState() == UserStateEnum.FROZEN) {
            userOperatorResponse.setSuccess(true);
            return userOperatorResponse;
        }
        //更新数据库
        user.setState(UserStateEnum.FROZEN);
        boolean updateResult = updateById(user);
        Assert.isTrue(updateResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
        //加入流水
        long result = userOperateStreamService.insertStream(user, UserOperateTypeEnum.FREEZE);
        Assert.notNull(result, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
        //第二次删除缓存
        idUserCache.remove(userId.toString());
        userOperatorResponse.setSuccess(true);
        return userOperatorResponse;
    }

    /**
     * 解冻
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse unfreeze(Long userId) {
        return null;
    }

    /**
     * 分页查询用户信息
     *
     * @param keyWord
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageResponse<User> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        //分页查询

        return null;
    }


    /**
     * 通过用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @Cached(name = ":user:cache:id:", cacheType = CacheType.BOTH, key = "#userId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES)
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.nickNameBloomFilter = redissonClient.getBloomFilter("nickName");
        if (nickNameBloomFilter != null && !nickNameBloomFilter.isExists()) {
            this.nickNameBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteCodeBloomFilter = redissonClient.getBloomFilter("inviteCode");
        if (inviteCodeBloomFilter != null && !inviteCodeBloomFilter.isExists()) {
            this.inviteCodeBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteRank = redissonClient.getScoredSortedSet("inviteRank");
    }
}
