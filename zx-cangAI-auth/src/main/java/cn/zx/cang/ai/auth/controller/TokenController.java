package cn.zx.cang.ai.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.zx.cang.ai.auth.exception.AuthErrorCode;
import cn.zx.cang.ai.auth.exception.AuthException;
import cn.zx.cang.ai.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author kinchou
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("token")
public class TokenController {

    @GetMapping("/get")
    public Result<String> get(@NotBlank String scene) {
        if (StpUtil.isLogin()) {
            //生成token
            String userId = StpUtil.getLoginIdAsString();
            //token保存到缓存中

            //返回token
            return Result.success(UUID.randomUUID().toString());
        }
        throw new AuthException(AuthErrorCode.USER_NOT_LOGIN);
    }
}
