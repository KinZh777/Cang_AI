package cn.zx.cang.ai.order.facade;

import cn.zx.cang.ai.api.order.OrderFacadeService;
import cn.zx.cang.ai.api.order.constant.OrderErrorCode;
import cn.zx.cang.ai.api.order.model.TradeOrderVO;
import cn.zx.cang.ai.api.order.request.*;
import cn.zx.cang.ai.api.order.response.OrderResponse;
import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.base.response.SingleResponse;
import cn.zx.cang.ai.lock.DistributeLock;
import cn.zx.cang.ai.order.domain.entity.convertor.TradeOrderConvertor;
import cn.zx.cang.ai.order.domain.exception.OrderException;
import cn.zx.cang.ai.order.domain.service.OrderReadService;
import cn.zx.cang.ai.rpc.facade.Facade;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kinchou
 */
@DubboService(version = "1.0.0")
public class OrderFacadeServiceImpl implements OrderFacadeService {

    @Autowired
    private OrderReadService orderReadService;

    @Override
    @DistributeLock(keyExpression = "#request.identifier", scene = "ORDER_CREATE")
    @Facade
    public OrderResponse create(OrderCreateRequest request) {

        //前置校验

        //库存预扣减

        //订单创建
        throw new OrderException(OrderErrorCode.INVENTORY_DEDUCT_FAILED);
    }

    @Override
    @Facade
    public OrderResponse cancel(OrderCancelRequest request) {
        //使用事务消息进行关单
        return null;
    }

    @Override
    @Facade
    public OrderResponse timeout(OrderTimeoutRequest request) {
        //使用事务消息进行关单
        return null;
    }

    @Override
    public OrderResponse confirm(OrderConfirmRequest request) {

        return null;
    }

    @Override
    @Facade
    public OrderResponse pay(OrderPayRequest request) {
        return null;
    }

    @Override
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId) {
        return SingleResponse.of(TradeOrderConvertor.INSTANCE.mapToVo(orderReadService.getOrder(orderId)));
    }

    @Override
    @Facade
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId, String userId) {
        return SingleResponse.of(TradeOrderConvertor.INSTANCE.mapToVo(orderReadService.getOrder(orderId, userId)));
    }

    @Override
    @Facade
    public PageResponse<TradeOrderVO> pageQuery(OrderPageQueryRequest request) {
        return null;
    }
}
