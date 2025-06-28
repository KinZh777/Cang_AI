package cn.zx.cang.ai.api.pay.response;

import cn.zx.cang.ai.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class PayCreateResponse extends BaseResponse {

    private String payOrderId;

    private String payUrl;
}
