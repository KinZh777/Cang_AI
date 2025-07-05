package cn.zx.cang.ai.auth.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.zx.cang.ai.api.notice.response.NoticeResponse;
import cn.zx.cang.ai.api.notice.service.NoticeFacadeService;
import cn.zx.cang.ai.api.user.request.UserQueryRequest;
import cn.zx.cang.ai.api.user.request.UserRegisterRequest;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.api.user.response.UserQueryResponse;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.api.user.service.UserFacadeService;
import cn.zx.cang.ai.auth.exception.AuthException;
import cn.zx.cang.ai.auth.param.LoginParam;
import cn.zx.cang.ai.auth.param.RegisterParam;
import cn.zx.cang.ai.auth.vo.LoginVO;
import cn.zx.cang.ai.web.vo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static cn.zx.cang.ai.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static cn.zx.cang.ai.auth.exception.AuthErrorCode.VERIFICATION_CODE_WRONG;

/**
 * 认证相关接口
 *
 * @author kinchou
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @DubboReference(version = "1.0.0")
    private NoticeFacadeService noticeFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    private static final String ROOT_CAPTCHA = "8888";
    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    @GetMapping("/sendCaptcha")
    public Result<Boolean> sendCaptcha(@NotBlank String telephone) {
        NoticeResponse noticeResponse = noticeFacadeService.generateAndSendSmsCaptcha(telephone);
        return Result.success(noticeResponse.getSuccess());
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterParam registerParam) {
        //验证码校验
        if(StringUtils.equalsIgnoreCase(registerParam.getCaptcha(),redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX+registerParam.getTelephone()))){
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }
        //校验通过 删除验证码
        redisTemplate.delete(CAPTCHA_KEY_PREFIX+registerParam.getTelephone());
        //注册 调用User模块的服务
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setTelephone(registerParam.getTelephone());
        userRegisterRequest.setInviteCode(registerParam.getInviteCode());

        UserOperatorResponse userOperatorResponse = userFacadeService.register(userRegisterRequest);
        //注册结果返回
        if(userOperatorResponse.getSuccess()){
            return Result.success(true);
        }
        return Result.error(userOperatorResponse.getResponseCode(), userOperatorResponse.getResponseMessage());
    }

    /**
     * 登录方法
     *
     * @param loginParam 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginParam loginParam) {
        //fixme 超级验证码8888
        if (!ROOT_CAPTCHA.equals(loginParam.getCaptcha())) {
            //验证码校验
            String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX + loginParam.getTelephone());
            if (!StringUtils.equalsIgnoreCase(cachedCode, loginParam.getCaptcha())) {
                throw new AuthException(VERIFICATION_CODE_WRONG);
            }
            //校验通过删除验证码
            redisTemplate.delete(CAPTCHA_KEY_PREFIX+loginParam.getTelephone());
        }
        //根据手机号查询用户
        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParam.getTelephone());
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        if(userInfo==null){
            //如果没有查到 -> 注册
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setTelephone(loginParam.getTelephone());
            UserOperatorResponse userOperatorResponse = userFacadeService.register(userRegisterRequest);
            if(userOperatorResponse.getSuccess()){
                //注册成功，重新查一把数据库获取用户信息
                userQueryResponse = userFacadeService.query(userQueryRequest);
                userInfo = userQueryResponse.getData();
            }else return Result.error(userOperatorResponse.getResponseCode(), userOperatorResponse.getResponseMessage());
        }
        StpUtil.login(userInfo.getUserId(), new SaLoginModel().setIsLastingCookie(loginParam.getRememberMe()).setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
        LoginVO loginVO = new LoginVO(userInfo);
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout() {
        //退出
        StpUtil.logout();
        //TODO 是否要删除缓存的用户信息？
        return Result.success(true);
    }

}
