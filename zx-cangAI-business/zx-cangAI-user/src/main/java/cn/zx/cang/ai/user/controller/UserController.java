package cn.zx.cang.ai.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.service.ChainFacadeService;
import cn.zx.cang.ai.api.user.request.UserActiveRequest;
import cn.zx.cang.ai.api.user.request.UserAuthRequest;
import cn.zx.cang.ai.api.user.request.UserModifyRequest;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
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

import static cn.zx.cang.ai.user.infrastructure.exception.UserErrorCode.*;

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

    private final static String APP_NAME_USER = "CANGAI_USER";

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ChainFacadeService chainFacadeService;

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
        Long userId = StpUtil.getLoginIdAsLong();
        //TODO 用户实名认证
        // 1. 进行实名认证 - > 实名认证成功推进用户状态为AUTH
        UserAuthRequest userAuthRequest = new UserAuthRequest();
        userAuthRequest.setUserId(userId);
        userAuthRequest.setIdCard(userAuthParam.getIdCard());
        userAuthRequest.setRealName(userAuthParam.getRealName());
        UserOperatorResponse authResponse = userService.auth(userAuthRequest);
        if(!authResponse.getSuccess()){
            return Result.error(authResponse.getResponseCode(),authResponse.getResponseMessage());
        }
        // 2. 用户上链 - > 链服务 - > 上链成功推进用户状态为ACTIVE
        ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
        chainProcessRequest.setUserId(String.valueOf(userId));
        String identifier = APP_NAME_USER + "_" + authResponse.getUser().getUserRole().name()+"_"+ authResponse.getUser().getUserId();
        chainProcessRequest.setIdentifier(identifier);
        ChainProcessResponse<ChainCreateData> createAddrResult = chainFacadeService.createAddr(chainProcessRequest);
        if (createAddrResult.getSuccess()) {
            //激活账户
            ChainCreateData chainCreateData = createAddrResult.getData();
            UserActiveRequest userActiveRequest = new UserActiveRequest();
            userActiveRequest.setUserId(Long.valueOf(userId));
            userActiveRequest.setBlockChainUrl(chainCreateData.getAccount());
            userActiveRequest.setBlockChainPlatform(chainCreateData.getPlatform());
            UserOperatorResponse activeResponse = userService.active(userActiveRequest);
            if (activeResponse.getSuccess()) {
                refreshUserInSession(String.valueOf(userId));
                return Result.success(true);
            }else {
                return Result.error(activeResponse.getResponseCode(), activeResponse.getResponseMessage());
            }
        }else {
            throw new UserException(USER_CREATE_CHAIN_FAIL);
        }
    }
    private void refreshUserInSession(String userId) {
        User user = userService.getById(userId);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
    }
}
