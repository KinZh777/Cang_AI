package cn.zx.cang.ai.api.collection.response;

import cn.zx.cang.ai.api.collection.constant.CollectionInventoryModifyType;
import lombok.Getter;
import lombok.Setter;

/**
 * 藏品库存修改响应
 *
 * @author kinchou
 */
@Getter
@Setter
public class CollectionInventoryModifyResponse extends CollectionModifyResponse {
    /**
     * 修改类型
     */
    private CollectionInventoryModifyType modifyType;

    /**
     * 修改的数量
     */
    private Long quantityModified;
}
