package cn.zx.cang.ai.api.goods.constant;


public enum GoodsState {
    /**
     * 不可售卖
     * <p>
     * 当前状态非可售卖
     */
    NOT_FOR_SALE,

    /**
     * 售卖中
     * <p>
     * 当前状态是可售卖，并且到达开售时间，并且有库存
     */
    SELLING,

    /**
     * 售空
     * <p>
     * 当前状态是可售卖，并且到达开售时间，但是没有库存
     */
    SOLD_OUT,

    /**
     * 即将开售
     * <p>
     * 当前状态是可售卖，并且有库存，但是到达开售时间，且距离开售时间小于1天
     */
    COMING_SOON,

    /**
     * 等待开售
     * <p>
     * 当前状态是可售卖，并且有库存，但是到达开售时间，且距离开售时间大于1天
     */
    WAIT_FOR_SALE
}
