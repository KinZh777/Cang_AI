package cn.zx.cang.ai.api.user.service;

import cn.zx.cang.ai.api.user.response.UserOperatorResponse;

/**
 * @author kinchou
 */
public interface UserManageFacadeService {

    /**
     * 用户冻结
     *
     * @param userId
     * @return
     */
    UserOperatorResponse freeze(Long userId);

    /**
     * 用户解冻
     *
     * @param userId
     * @return
     */
    UserOperatorResponse unfreeze(Long userId);

}
