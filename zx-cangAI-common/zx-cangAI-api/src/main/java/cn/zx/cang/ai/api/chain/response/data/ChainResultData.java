package cn.zx.cang.ai.api.chain.response.data;

import java.io.Serializable;

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
public class ChainResultData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ntf唯一编号
     */
    private String nftId;

    /**
     * 交易哈希
     */
    private String txHash;

    /**
     * 状态
     */
    private String state;

    /**
     * '藏品编号'
     */
    private String serialNo;


}
