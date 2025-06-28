package cn.zx.cang.ai.api.user.request;

import cn.zx.cang.ai.api.user.request.condition.UserIdQueryCondition;
import cn.zx.cang.ai.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import cn.zx.cang.ai.api.user.request.condition.UserPhoneQueryCondition;
import cn.zx.cang.ai.api.user.request.condition.UserQueryCondition;
import cn.zx.cang.ai.base.request.BaseRequest;
import lombok.*;

/**
 * @author kinchou
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String telephone, String password) {
        UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition = new UserPhoneAndPasswordQueryCondition();
        userPhoneAndPasswordQueryCondition.setTelephone(telephone);
        userPhoneAndPasswordQueryCondition.setPassword(password);
        this.userQueryCondition = userPhoneAndPasswordQueryCondition;
    }

}
