package cn.zx.cang.ai.order.domain.listener.event;

import cn.zx.cang.ai.order.domain.entity.TradeOrder;
import org.springframework.context.ApplicationEvent;

/**
 * @author kinchou
 */
public class OrderCreateEvent extends ApplicationEvent {

    public OrderCreateEvent(TradeOrder tradeOrder) {
        super(tradeOrder);
    }
}
