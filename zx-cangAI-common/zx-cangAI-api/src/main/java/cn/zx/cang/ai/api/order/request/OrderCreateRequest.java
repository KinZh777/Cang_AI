package cn.zx.cang.ai.api.order.request;

import cn.zx.cang.ai.api.goods.constant.GoodsType;
import cn.zx.cang.ai.api.order.constant.TradeOrderEvent;
import cn.zx.cang.ai.api.user.constant.UserType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author kinchou
 */
@Getter
@Setter
public class OrderCreateRequest extends BaseOrderRequest {

    /**
     * 买家id
     */
    @NotNull(message = "买家id不能为空")
    private String buyerId;

    /**
     * 买家id类型
     */
    private UserType buyerType = UserType.CUSTOMER;

    /**
     * 卖家id
     */
    @NotNull(message = "卖家id不能为空")
    private String sellerId;

    /**
     * 卖家id类型
     */
    private UserType sellerType = UserType.PLATFORM;

    /**
     * 订单金额
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "订单金额必须大于0")
    private BigDecimal orderAmount;

    /**
     * 商品Id
     */
    @NotNull(message = "商品Id不能为空")
    private String goodsId;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 商品图片
     */
    private String goodsPicUrl;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    @Min(value = 1)
    private int itemCount;

    /**
     * 商品单价
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "商品单价必须大于0")
    private BigDecimal itemPrice;

    /**
     * 快照版本
     */
    private Integer snapshotVersion;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CREATE;
    }
}

