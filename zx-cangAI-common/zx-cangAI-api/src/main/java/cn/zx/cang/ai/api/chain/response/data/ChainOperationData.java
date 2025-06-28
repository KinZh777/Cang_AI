package cn.zx.cang.ai.api.chain.response.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zh_X
 * @date 2025/01/18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChainOperationData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作编号
     */
    private String operationId;

}
