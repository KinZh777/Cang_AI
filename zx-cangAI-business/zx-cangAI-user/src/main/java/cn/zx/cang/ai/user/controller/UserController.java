package cn.zx.cang.ai.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.domain.entity.convertor.UserConvertor;
import cn.zx.cang.ai.user.domain.service.UserService;
import cn.zx.cang.ai.user.infrastructure.exception.UserException;
import cn.zx.cang.ai.user.param.UserModifyParam;
import cn.zx.cang.ai.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.USER_NOT_EXIST;

/**
 * 用户信息
 *
 * @author
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));

        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        return Result.success(UserConvertor.INSTANCE.mapToVo(user));
    }

    @PostMapping("/modifyNickName")
    public Result<Boolean> modifyNickName(@Valid @RequestBody UserModifyParam userModifyParam) {
        //从session中查询用户ID

        //修改信息
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("/modifyPassword")
    public Result<Boolean> modifyPassword(@Valid @RequestBody UserModifyParam userModifyParam) {
        //从session中查询用户ID

        //修改信息
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("/modifyProfilePhoto")
    public Result<String> modifyProfilePhoto(@RequestParam("file_data") MultipartFile file) throws Exception {
        //从session中查询用户ID

        //修改信息
        return Result.success(UUID.randomUUID().toString());
    }
}
