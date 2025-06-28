package cn.zx.cang.ai.api.user.request;

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
public class UserRegisterRequest extends BaseRequest {

    private String telephone;

    private String inviteCode;

    private String password;

}
