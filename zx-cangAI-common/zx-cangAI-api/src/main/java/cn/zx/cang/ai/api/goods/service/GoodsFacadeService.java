package cn.zx.cang.ai.api.goods.service;

import cn.zx.cang.ai.api.goods.constant.GoodsType;
import cn.zx.cang.ai.api.goods.model.BaseGoodsVO;

/**
 * 商品服务
 *
 * @author kinchou
 */
public interface GoodsFacadeService {

    /**
     * 获取商品
     *
     * @param goodsId
     * @param goodsType
     * @return
     */
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType);

}
