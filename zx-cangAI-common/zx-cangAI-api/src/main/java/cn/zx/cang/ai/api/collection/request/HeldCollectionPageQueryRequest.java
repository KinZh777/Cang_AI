package cn.zx.cang.ai.api.collection.request;

import cn.zx.cang.ai.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Getter
@Setter
public class HeldCollectionPageQueryRequest extends PageRequest {

    private String state;

    private String userId;

    private String keyword;
}
