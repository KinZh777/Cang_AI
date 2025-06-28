package cn.zx.cang.ai.api.collection.request;

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
public class CollectionModifyInventoryRequest extends BaseCollectionRequest {

    /**
     * '藏品数量'
     */
    private Long quantity;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.MODIFY_INVENTORY;
    }
}
