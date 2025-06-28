package cn.zx.cang.ai.api.chain.service;

import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.response.data.ChainOperationData;

/**
 * @author kinchou
 */
public interface ChainFacadeService {

    /**
     * 创建链账户
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request);

    /**
     * 上链藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest request);

    /**
     * 铸造藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request);

    /**
     * 交易藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request);

    /**
     * 销毁藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request);
}
