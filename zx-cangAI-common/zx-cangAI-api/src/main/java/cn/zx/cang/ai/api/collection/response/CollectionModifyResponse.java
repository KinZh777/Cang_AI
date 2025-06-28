package cn.zx.cang.ai.api.collection.response;

import cn.zx.cang.ai.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class CollectionModifyResponse extends BaseResponse {
    /**
     * 藏品id
     */
    private Long collectionId;
}
