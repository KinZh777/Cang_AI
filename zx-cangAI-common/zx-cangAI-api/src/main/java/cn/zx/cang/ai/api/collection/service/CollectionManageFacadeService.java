package cn.zx.cang.ai.api.collection.service;

import cn.zx.cang.ai.api.collection.model.CollectionVO;
import cn.zx.cang.ai.api.collection.request.*;
import cn.zx.cang.ai.api.collection.response.CollectionChainResponse;
import cn.zx.cang.ai.api.collection.response.CollectionModifyResponse;
import cn.zx.cang.ai.api.collection.response.CollectionRemoveResponse;
import cn.zx.cang.ai.base.response.PageResponse;

/**
 * 藏品管理门面服务
 *
 * @author kinchou
 */
public interface CollectionManageFacadeService {

    /**
     * 创建藏品
     *
     * @param request
     * @return
     */
    CollectionChainResponse create(CollectionCreateRequest request);


    /**
     * 藏品下架
     *
     * @param request
     * @return
     */
    CollectionRemoveResponse remove(CollectionRemoveRequest request);


    /**
     * 藏品库存修改
     *
     * @param request
     * @return
     */
    CollectionModifyResponse modifyInventory(CollectionModifyInventoryRequest request);

    /**
     * 藏品价格修改
     *
     * @param request
     * @return
     */
    CollectionModifyResponse modifyPrice(CollectionModifyPriceRequest request);

    /**
     * 藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request);
}
