package cn.zx.cang.ai.api.collection.service;

import cn.zx.cang.ai.api.collection.model.CollectionInventoryVO;
import cn.zx.cang.ai.api.collection.model.CollectionVO;
import cn.zx.cang.ai.api.collection.model.HeldCollectionVO;
import cn.zx.cang.ai.api.collection.request.*;
import cn.zx.cang.ai.api.collection.response.CollectionDestroyResponse;
import cn.zx.cang.ai.api.collection.response.CollectionSaleResponse;
import cn.zx.cang.ai.api.collection.response.CollectionTransferResponse;
import cn.zx.cang.ai.base.response.PageResponse;
import cn.zx.cang.ai.base.response.SingleResponse;

/**
 * 藏品门面服务
 *
 * @author kinchou
 */
public interface CollectionFacadeService {

    /**
     * 藏品出售的try阶段，做库存预占用
     *
     * @param request
     * @return
     */
    CollectionSaleResponse trySale(CollectionSaleRequest request);

    /**
     * 藏品出售的confirm阶段，做真正售出
     *
     * @param request
     * @return
     */
    CollectionSaleResponse confirmSale(CollectionSaleRequest request);

    /**
     * 藏品出售的cancel阶段，做库存退还
     *
     * @param request
     * @return
     */
    CollectionSaleResponse cancelSale(CollectionSaleRequest request);

    /**
     * 转移藏品
     *
     * @param request
     * @return
     */
    CollectionTransferResponse transfer(CollectionTransferRequest request);

    /**
     * 销毁藏品
     *
     * @param request
     * @return
     */
    CollectionDestroyResponse destroy(CollectionDestroyRequest request);

    /**
     * 根据Id查询藏品
     *
     * @param collectionId
     * @return
     */
    public SingleResponse<CollectionVO> queryById(Long collectionId);

    /**
     * 预扣减库存
     *
     * @param collectionId 藏品id
     * @param quantity     数量
     * @param identifier   唯一标识
     * @return
     */
    public SingleResponse<Boolean> preInventoryDeduct(Long collectionId, int quantity, String identifier);

    /**
     * 查询藏品库存
     *
     * @param collectionId
     * @return
     */
    public SingleResponse<CollectionInventoryVO> queryInventory(Long collectionId);

    /**
     * 藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request);

    /**
     * 持有藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<HeldCollectionVO> pageQueryHeldCollection(HeldCollectionPageQueryRequest request);

    /**
     * 根据id查询持有藏品
     *
     * @param heldCollectionId
     * @return
     */
    public SingleResponse<HeldCollectionVO> queryHeldCollectionById(Long heldCollectionId);

}
