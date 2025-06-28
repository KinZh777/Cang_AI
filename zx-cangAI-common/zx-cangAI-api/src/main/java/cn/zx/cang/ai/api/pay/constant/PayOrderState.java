package cn.zx.cang.ai.api.pay.constant;

/**
 * @author kinchou
 */
public enum PayOrderState {

    /**
     * 待支付
     */
    TO_PAY,

    /**
     * 支付中
     */
    PAYING,

    /**
     * 已付款
     */
    PAID,

    /**
     * 支付超时
     */
    EXPIRED,

    /**
     * 已退款
     */
    REFUNDED;
}
