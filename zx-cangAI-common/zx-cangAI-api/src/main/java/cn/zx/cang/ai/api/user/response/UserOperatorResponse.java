package cn.zx.cang.ai.api.user.response;

import cn.zx.cang.ai.api.user.response.data.UserInfo;
import cn.zx.cang.ai.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @author kinchou
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
