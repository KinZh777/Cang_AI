package cn.zx.cang.ai.user.facade;

import cn.zx.cang.ai.api.user.request.*;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.api.user.response.UserQueryResponse;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.api.user.service.UserFacadeService;
import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.rpc.facade.Facade;
import cn.zx.cang.ai.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kinchou
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Facade
    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {

        return null;
    }

    @Facade
    @Override
    public PageResponse<UserInfo> pageQuery(UserPageQueryRequest userPageQueryRequest) {

        return null;
    }

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.getTelephone(), userRegisterRequest.getInviteCode());
    }

    @Override
    @Facade
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        return null;
    }

    @Override
    @Facade
    public UserOperatorResponse auth(UserAuthRequest userAuthRequest) {
        return userService.auth(userAuthRequest);
    }

    @Override
    @Facade
    public UserOperatorResponse active(UserActiveRequest userActiveRequest) {
        return userService.active(userActiveRequest);
    }
}
