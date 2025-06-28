package cn.zx.cang.ai.api.pay.response;

import cn.zx.cang.ai.api.pay.model.PayOrderVO;
import cn.zx.cang.ai.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author kinchou
 */
@Getter
@Setter
public class PayQueryResponse extends BaseResponse {

    private List<PayOrderVO> payOrders;
}
