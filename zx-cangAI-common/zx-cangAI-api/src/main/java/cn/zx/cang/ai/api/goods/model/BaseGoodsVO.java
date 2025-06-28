package cn.zx.cang.ai.api.goods.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author kinchou
 */
public abstract class BaseGoodsVO implements Serializable {
    /**
     * 商品名称
     *
     * @return
     */
    public abstract String getGoodsName();

    /**
     * 商品图片
     *
     * @return
     */
    public abstract String getGoodsPicUrl();

    /**
     * 卖家id
     *
     * @return
     */
    public abstract String getSellerId();

    /**
     * 版本
     *
     * @return
     */
    public abstract Integer getVersion();

    /**
     * 是否可用
     *
     * @return
     */
    public abstract Boolean available();

    /**
     * 价格
     *
     * @return
     */
    public abstract BigDecimal getPrice();
}
