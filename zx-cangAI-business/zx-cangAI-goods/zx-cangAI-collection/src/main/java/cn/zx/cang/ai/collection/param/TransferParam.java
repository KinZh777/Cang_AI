package cn.zx.cang.ai.collection.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferParam {
    @NotBlank(message = "collectionId 不能为空")
    private String collectionId;
    @NotBlank(message = "recipientUserId 不能为空")
    private Long recipientUserId;
}
