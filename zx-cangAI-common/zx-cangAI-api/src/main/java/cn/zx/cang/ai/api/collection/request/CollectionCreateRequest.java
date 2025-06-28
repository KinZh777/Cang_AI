package cn.zx.cang.ai.api.collection.request;

import java.math.BigDecimal;
import java.util.Date;

import cn.zx.cang.ai.api.collection.constant.CollectionEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author kinchou
 * @date 2025/01/17
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCreateRequest extends BaseCollectionRequest {

    /**
     * '藏品名称'
     */
    private String name;

    /**
     * '藏品封面'
     */
    private String cover;

    /**
     * '藏品详情'
     */
    private String detail;

    /**
     * '价格'
     */
    private BigDecimal price;

    /**
     * '藏品数量'
     */
    private Long quantity;

    /**
     * '藏品创建时间'
     */
    private Date createTime;

    /**
     * '藏品发售时间'
     */
    private Date saleTime;

    /**
     * '藏品创建者id'
     */
    private String creatorId;

    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.CHAIN;
    }
}
