package cn.zx.cang.ai.api.user.request;

import cn.zx.cang.ai.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author kinchou
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveRequest extends BaseRequest {

    private Long userId;
    private String blockChainPlatform;
    private String blockChainUrl;

}
