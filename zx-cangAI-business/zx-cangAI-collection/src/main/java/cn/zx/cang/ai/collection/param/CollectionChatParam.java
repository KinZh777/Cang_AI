package cn.zx.cang.ai.collection.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionChatParam {
    private String collectionId;
    private String message;
    private Long userId;

    private String conversationId;
}
