package cn.zx.cang.ai.order.domain.entity;

import cn.zx.cang.ai.api.goods.constant.GoodsType;
import cn.zx.cang.ai.api.order.constant.TradeOrderEvent;
import cn.zx.cang.ai.api.order.constant.TradeOrderState;
import cn.zx.cang.ai.api.order.request.*;
import cn.zx.cang.ai.api.pay.constant.PayChannel;
import cn.zx.cang.ai.api.user.constant.UserType;
import cn.zx.cang.ai.datasource.domain.entity.BaseEntity;
import cn.zx.cang.ai.datasource.sharding.id.BusinessCode;
import cn.zx.cang.ai.datasource.sharding.id.DistributeID;
import cn.zx.cang.ai.order.domain.entity.convertor.TradeOrderConvertor;
import cn.zx.cang.ai.order.domain.entity.statemachine.OrderStateMachine;
import cn.zx.cang.ai.order.infrastructure.id.WorkerIdHolder;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kinchou
 */
@Setter
@Getter
public class TradeOrder extends BaseEntity {

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_TIME_OUT_MINUTES = 30;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 买家id
     */
    private String buyerId;

    /**
     * 买家 ID 的逆序
     */
    private String reverseBuyerId;

    /**
     * 买家id类型
     */
    private UserType buyerType;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 卖家id类型
     */
    private UserType sellerType;

    /**
     * 幂等号
     */
    private String identifier;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 商品数量
     */
    private int itemCount;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 支付成功时间
     */
    private Date paySucceedTime;

    /**
     * 下单确认时间
     */
    private Date orderConfirmedTime;

    /**
     * 下单确认时间
     */
    private Date orderFinishedTime;

    /**
     * 订单关闭时间
     */
    private Date orderClosedTime;

    /**
     * 商品Id
     */
    private String goodsId;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsPicUrl;

    /**
     * 支付方式
     */
    private PayChannel payChannel;

    /**
     * 支付流水号
     */
    private String payStreamId;

    /**
     * 订单状态
     */
    private TradeOrderState orderState;

    /**
     * 关单类型
     */
    private String closeType;

    /**
     * 快照版本
     */
    private Integer snapshotVersion;

    @JSONField(serialize = false)
    public Boolean isPaid() {
        return orderState == TradeOrderState.FINISH || orderState == TradeOrderState.PAID;
    }

    @JSONField(serialize = false)
    public Boolean isConfirmed() {
        return orderState == TradeOrderState.CONFIRM;
    }

    @JSONField(serialize = false)
    public Boolean isTimeout() {
        //订单已关闭 (订单未支付且未关闭 并且 订单已经达到了超时时间)
        return (orderState == TradeOrderState.CLOSED && closeType == TradeOrderEvent.TIME_OUT.name())
                || (orderState == TradeOrderState.CONFIRM && this.getGmtCreate().compareTo(DateUtils.addMinutes(new Date(), -TradeOrder.DEFAULT_TIME_OUT_MINUTES)) < 0);
    }

    @JSONField(serialize = false)
    public Date getPayExpireTime() {
        return DateUtils.addMinutes(this.getGmtCreate(), TradeOrder.DEFAULT_TIME_OUT_MINUTES);
    }

    public static TradeOrder createOrder(OrderCreateRequest request) {
        TradeOrder tradeOrder = TradeOrderConvertor.INSTANCE.mapToEntity(request);
        tradeOrder.setReverseBuyerId(StringUtils.reverse(request.getBuyerId()));
        tradeOrder.setOrderState(TradeOrderState.CREATE);
        tradeOrder.setPaidAmount(BigDecimal.ZERO);
        String orderId = DistributeID.generateWithSnowflake(BusinessCode.TRADE_ORDER, WorkerIdHolder.WORKER_ID, request.getBuyerId());
        tradeOrder.setOrderId(orderId);
        return tradeOrder;
    }

    public TradeOrder confirm(OrderConfirmRequest request) {
        this.setOrderConfirmedTime(request.getOperateTime());
        TradeOrderState orderState = OrderStateMachine.INSTANCE.transition(this.getOrderState(), request.getOrderEvent());
        this.setOrderState(orderState);
        return this;
    }

    public TradeOrder pay(OrderPayRequest request) {
        this.setPayStreamId(request.getPayStreamId());
        this.setPaySucceedTime(request.getOperateTime());
        this.setPayChannel(request.getPayChannel());
        this.setPaidAmount(request.getAmount());
        TradeOrderState orderState = OrderStateMachine.INSTANCE.transition(this.getOrderState(), request.getOrderEvent());
        this.setOrderState(orderState);
        return this;
    }

    public TradeOrder close(BaseOrderUpdateRequest request) {
        this.setOrderClosedTime(request.getOperateTime());
        TradeOrderState orderState = OrderStateMachine.INSTANCE.transition(this.getOrderState(), request.getOrderEvent());
        this.setOrderState(orderState);
        this.setCloseType(request.getOrderEvent().name());
        return this;
    }

    public TradeOrder finish(OrderFinishRequest request) {
        this.setOrderFinishedTime(request.getOperateTime());
        TradeOrderState orderState = OrderStateMachine.INSTANCE.transition(this.getOrderState(), request.getOrderEvent());
        this.setOrderState(orderState);
        return this;
    }
}
