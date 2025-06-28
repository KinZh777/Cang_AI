package cn.zx.cang.ai.order.domain.listener.event;

import cn.zx.cang.ai.api.order.request.BaseOrderRequest;
import org.springframework.context.ApplicationEvent;

/**
 * @author kinchou
 */
public class OrderTimeoutEvent extends ApplicationEvent {

    public OrderTimeoutEvent(BaseOrderRequest baseOrderRequest) {
        super(baseOrderRequest);
    }
}
