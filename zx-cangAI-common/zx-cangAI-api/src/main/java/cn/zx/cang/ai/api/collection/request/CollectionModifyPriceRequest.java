package cn.zx.cang.ai.api.collection.request;

import java.math.BigDecimal;

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
public class CollectionModifyPriceRequest extends BaseCollectionRequest {

    /**
     * '价格'
     */
    private BigDecimal price;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.MODIFY_PRICE;
    }
}
