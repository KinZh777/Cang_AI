package cn.zx.cang.ai.user.facade;

import cn.zx.cang.ai.api.user.response.UserOperatorResponse;
import cn.zx.cang.ai.api.user.service.UserManageFacadeService;
import cn.zx.cang.ai.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kinchou
 */
@DubboService(version = "1.0.0")
public class UserManageFacadeServiceImpl implements UserManageFacadeService {

    @Autowired
    private UserService userService;

    @Override
    public UserOperatorResponse freeze(Long userId) {
        return userService.freeze(userId);
    }

    @Override
    public UserOperatorResponse unfreeze(Long userId) {
        return userService.unfreeze(userId);
    }
}
