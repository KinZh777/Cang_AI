package cn.zx.cang.ai.chain.domain.constant;

public enum ChainCodeEnum {
    /**
     * 上链成功
     */
    SUCCESS,

    /**
     * 上链中
     */
    PROCESSING,

    /**
     * 请求异常
     */
    CHAIN_POST_ERROR,

    /**
     * 结果格式错误
     */
    CHAIN_RESULT_NOT_JSON,

    /**
     * 结果错误
     */
    CHAIN_RESULT_ERROR
}
