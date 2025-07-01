package cn.zx.cang.ai.user.domain.service;

import cn.zx.cang.ai.api.user.constant.UserOperateTypeEnum;
import cn.zx.cang.ai.api.user.request.UserActiveRequest;
import cn.zx.cang.ai.api.user.request.UserAuthRequest;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.base.exception.BizException;
import cn.zx.cang.ai.base.exception.RepoErrorCode;
import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.lock.DistributeLock;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode;
import cn.zx.cang.ai.user.infrastructure.exception.UserException;
import cn.zx.cang.ai.user.infrastructure.mapper.UserMapper;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.DUPLICATE_TELEPHONE_NUMBER;

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
    private RedissonClient redissonClient;

    private RBloomFilter<String> nickNameBloomFilter;

    private RBloomFilter<String> inviteCodeBloomFilter;

    private RScoredSortedSet<String> inviteRank;

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, User> idUserCache;

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
    public UserOperatorResponse auth(UserAuthRequest userAuthRequest) {

        return null;
    }

    /**
     * 用户激活
     *
     * @param userActiveRequest
     * @return
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userActiveRequest.userId")
    public UserOperatorResponse active(UserActiveRequest userActiveRequest) {
        return null;
    }

    /**
     * 冻结
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse freeze(Long userId) {
        //查询用户

        //第一次删除缓存

        //更新数据库

        //加入流水

        //第二次删除缓存
        return null;
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
        return null;
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
