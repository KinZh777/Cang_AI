package cn.zx.cang.ai.api.order.request;

import cn.zx.cang.ai.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class OrderPageQueryRequest extends PageRequest {

    /**
     * 买家id
     */
    private String buyerId;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单状态
     */
    private String state;
}
