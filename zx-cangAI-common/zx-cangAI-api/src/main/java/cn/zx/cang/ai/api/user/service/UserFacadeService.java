package cn.zx.cang.ai.api.user.service;

import cn.zx.cang.ai.api.user.request.*;
import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.api.user.response.UserQueryResponse;
import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.base.response.PageResponse;

/**
 * @author kinchou
 */
public interface UserFacadeService {

    /**
     * 用户信息查询
     * @param userQueryRequest
     * @return
     */
    UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest);


    /**
     * 分页查询用户信息
     * @param userPageQueryRequest
     * @return
     */
    PageResponse<UserInfo> pageQuery(UserPageQueryRequest userPageQueryRequest);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户信息修改
     * @param userModifyRequest
     * @return
     */
    UserOperatorResponse modify(UserModifyRequest userModifyRequest);

    /**
     * 用户实名认证
     * @param userAuthRequest
     * @return
     */
    UserOperatorResponse auth(UserAuthRequest userAuthRequest);

    /**
     * 用户激活
     * @param userActiveRequest
     * @return
     */
    UserOperatorResponse active(UserActiveRequest userActiveRequest);

}
