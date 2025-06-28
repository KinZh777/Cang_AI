package cn.zx.cang.ai.api.collection.request;

import cn.zx.cang.ai.api.collection.constant.CollectionEvent;
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
@NoArgsConstructor
public class CollectionRemoveRequest extends BaseCollectionRequest {

    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.REMOVE;
    }
}
