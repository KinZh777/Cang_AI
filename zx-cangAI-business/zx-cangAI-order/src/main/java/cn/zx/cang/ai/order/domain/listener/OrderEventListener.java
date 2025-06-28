package cn.zx.cang.ai.order.domain.listener;

import cn.zx.cang.ai.api.order.OrderFacadeService;
import cn.zx.cang.ai.api.order.request.OrderConfirmRequest;
import cn.zx.cang.ai.api.user.constant.UserType;
import cn.zx.cang.ai.order.domain.entity.TradeOrder;
import cn.zx.cang.ai.order.domain.listener.event.OrderCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kinchou
 */
@Component
public class OrderEventListener {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @EventListener(OrderCreateEvent.class)
    @Async("orderListenExecutor")
    public void onApplicationEvent(OrderCreateEvent event) {

        TradeOrder tradeOrder = (TradeOrder) event.getSource();
        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setOperator(UserType.PLATFORM.name());
        confirmRequest.setOperatorType(UserType.PLATFORM);
        confirmRequest.setOrderId(tradeOrder.getOrderId());
        confirmRequest.setIdentifier(tradeOrder.getIdentifier());
        confirmRequest.setOperateTime(new Date());

        orderFacadeService.confirm(confirmRequest);
    }
}
