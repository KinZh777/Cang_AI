package cn.zx.cang.ai.api.pay.request;

import cn.zx.cang.ai.api.pay.constant.PayOrderState;
import cn.zx.cang.ai.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class PayQueryRequest extends BaseRequest {

    @NotNull(message = "payQueryCondition is null")
    private PayQueryCondition payQueryCondition;

    private PayOrderState payOrderState;

    @NotNull(message = "payerId is null")
    private String payerId;

}
