package cn.zx.cang.ai.collection.domain.entity;

import cn.hutool.core.convert.impl.CollectionConverter;
import cn.zx.cang.ai.api.collection.constant.CollectionStateEnum;
import cn.zx.cang.ai.api.collection.request.CollectionCreateRequest;
import cn.zx.cang.ai.collection.domain.entity.convertor.CollectionConvertor;
import cn.zx.cang.ai.datasource.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection")
public class Collection extends BaseEntity{

    private String name;

    private String cover;

    private String classId;

    private BigDecimal price;

    /**
     * 藏品总数量
     */
    private Long quantity;

    private String details;

    /**
     * 可售数量
     */
    private Long saleableInventory;

    /**
     * 已占库存
     */
    @Deprecated
    private Long occupiedInventory;

    private Long frozenInventory;

    private CollectionStateEnum state;

    private Date createTime;

    private Date saleTime;

    /**
     * 上链时间
     */
    private Date syncChainTime;

    private String creatorId;

    /**
     * 非乐观锁版本
     * 用于记录藏品快照 当价格、库存等重要信息修改时
     */
    private Integer version;

    private Date bookStartTime;

    private Date bookEndTime;

    private Boolean canBook;

    private String botId;

    public static Collection create(CollectionCreateRequest request){
        Collection collection = CollectionConvertor.INSTANCE.mapToEntity(request);
        collection.setOccupiedInventory(0L);
        collection.setSaleableInventory(request.getQuantity());
        collection.setState(CollectionStateEnum.INIT);
        collection.setVersion(1);
        return collection;
    }

    public Collection remove(){
        this.state = CollectionStateEnum.REMOVED;
        return this;
    }
}
