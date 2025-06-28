package cn.zx.cang.ai.api.collection.response;

import cn.zx.cang.ai.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class CollectionSaleResponse extends BaseResponse {
    /**
     * 持有藏品id
     */
    private Long heldCollectionId;

}
