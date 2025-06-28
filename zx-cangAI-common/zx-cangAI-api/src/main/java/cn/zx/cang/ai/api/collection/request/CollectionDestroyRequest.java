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
public class CollectionDestroyRequest extends BaseCollectionRequest {
    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;

    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.DESTROY;
    }
}
