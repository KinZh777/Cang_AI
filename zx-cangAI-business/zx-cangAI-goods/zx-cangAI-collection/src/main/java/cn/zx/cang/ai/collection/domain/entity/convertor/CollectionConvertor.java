package cn.zx.cang.ai.collection.domain.entity.convertor;

import cn.zx.cang.ai.api.collection.constant.CollectionStateEnum;
import cn.zx.cang.ai.api.collection.model.CollectionVO;
import cn.zx.cang.ai.api.collection.request.CollectionCreateRequest;
import cn.zx.cang.ai.api.goods.constant.GoodsState;
import cn.zx.cang.ai.collection.domain.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

@Mapper
public interface CollectionConvertor {
    CollectionConvertor INSTANCE = Mappers.getMapper(CollectionConvertor.class);

    /**
     * 转换为VO
     *
     * @param collection
     * @return
     */
    @Mapping(target = "inventory", source = "request.saleableInventory")
    @Mapping(target = "state", expression = "java(setState(request.getState(), request.getSaleTime(), request.getSaleableInventory()))")
    public CollectionVO mapToVo(Collection collection);

    /**
     * 转换为实体
     * @param collectionVO
     * @return
     */
    @Mapping(target = "saleableInventory", source = "request.inventory")
    @Mapping(target = "state", ignore = true)
    public Collection mapToEntity(CollectionVO collectionVO);

    /**
     * 设置状态
     * @param state
     * @param saleTime
     * @param saleableInventory
     * @return
     */
    public default GoodsState setState(CollectionStateEnum state, Date saleTime, Long saleableInventory) {
        return CollectionVO.getState(state, saleTime, saleableInventory);
    }

    /**
     * 创建快照
     *
     * @param
     * @return
     */
//    @Mapping(target = "collectionId", source = "request.id")
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "gmtCreate", ignore = true)
//    @Mapping(target = "gmtModified", ignore = true)
//    public CollectionSnapshot createSnapshot(Collection collection);

    /**
     * 转换为实体
     *
     * @param collectionCreateRequest
     * @return
     */
    public Collection mapToEntity(CollectionCreateRequest collectionCreateRequest);

    /**
     * collection转换为VO
     *
     * @param collections
     * @return
     */
    public List<CollectionVO> mapToVo(List<Collection> collections);
}
