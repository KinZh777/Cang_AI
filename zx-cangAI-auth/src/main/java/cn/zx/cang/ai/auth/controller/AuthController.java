package cn.zx.cang.ai.auth.controller;

import cn.zx.cang.ai.api.notice.response.NoticeResponse;
import cn.zx.cang.ai.api.notice.service.NoticeFacadeService;
import cn.zx.cang.ai.auth.param.LoginParam;
import cn.zx.cang.ai.auth.param.RegisterParam;
import cn.zx.cang.ai.auth.vo.LoginVO;
import cn.zx.cang.ai.web.vo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
        //注册
        //注册结果返回
        return Result.success(true);
    }

    /**
     * 登录方法
     *
     * @param loginParam 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginParam loginParam) {
        //判断是注册还是登陆
        //查询用户信息
        //登录
        //返回结果
        LoginVO loginVO = new LoginVO();
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout() {
        //退出
        return Result.success(true);
    }

}
