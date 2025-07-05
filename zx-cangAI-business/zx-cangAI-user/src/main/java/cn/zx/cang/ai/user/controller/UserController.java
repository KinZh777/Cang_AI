package cn.zx.cang.ai.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.zx.cang.ai.api.user.request.UserModifyRequest;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.file.FileService;
import cn.zx.cang.ai.user.domain.entity.User;
import cn.zx.cang.ai.user.domain.entity.convertor.UserConvertor;
import cn.zx.cang.ai.user.domain.service.UserService;
import cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode;
import cn.zx.cang.ai.user.infrastructure.exception.UserException;
import cn.zx.cang.ai.user.param.UserAuthParam;
import cn.zx.cang.ai.user.param.UserModifyParam;
import cn.zx.cang.ai.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.USER_NOT_EXIST;
import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.USER_UPLOAD_PICTURE_FAIL;

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

    @Autowired
    private FileService fileService;

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
        String userId = (String) StpUtil.getLoginId();
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setNickName(userModifyParam.getNickName());

        Boolean modifyResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(modifyResult);
    }

    @PostMapping("/modifyPassword")
    public Result<Boolean> modifyPassword(@Valid @RequestBody UserModifyParam userModifyParam) {
        //从session中查询用户ID
        String userId = (String) StpUtil.getLoginId();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            throw new UserException(USER_NOT_EXIST);
        }
        //旧密码校验
        if (!StringUtils.equals(user.getPasswordHash(), DigestUtil.md5Hex(userModifyParam.getOldPassword()))) {
            throw new UserException(UserErrorCode.USER_PASSWD_CHECK_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setPassword(userModifyParam.getNewPassword());

        Boolean modifyResult = userService.modify(userModifyRequest).getSuccess();
        return Result.success(modifyResult);
    }

    @PostMapping("/modifyProfilePhoto")
    public Result<String> modifyProfilePhoto(@RequestParam("file_data") MultipartFile file) throws Exception {
        String userId = (String) StpUtil.getLoginId();
        String prefix = "https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/";

        if (null == file) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        String filename = file.getOriginalFilename();
        InputStream fileStream = file.getInputStream();
        String path = "profile/" + userId + "/" + filename;
        var res = fileService.upload(path, fileStream);
        if (!res) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        //修改信息
        UserModifyRequest userModifyRequest = new UserModifyRequest();
        userModifyRequest.setUserId(Long.valueOf(userId));
        userModifyRequest.setProfilePhotoUrl(prefix + path);
        Boolean registerResult = userService.modify(userModifyRequest).getSuccess();
        if (!registerResult) {
            throw new UserException(USER_UPLOAD_PICTURE_FAIL);
        }
        return Result.success(prefix + path);
    }

    @PostMapping("/auth")
    public Result<Boolean> auth(@Valid @RequestBody UserAuthParam userAuthParam){
        //TODO 用户实名认证
        return Result.success(Boolean.TRUE);
    }
}
