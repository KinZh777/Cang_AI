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
public class CollectionTransferRequest extends BaseCollectionRequest {

    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;

    /**
     * '买家id'
     */
    private Long buyerId;

    /**
     * '卖家id'
     */
    private Long sellerId;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.TRANSFER;
    }
}
