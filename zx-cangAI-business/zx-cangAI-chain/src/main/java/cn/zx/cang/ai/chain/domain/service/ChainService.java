package cn.zx.cang.ai.chain.domain.service;

import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.request.ChainQueryRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.response.data.ChainResultData;
import cn.zx.cang.ai.chain.domain.entity.ChainOperateInfo;
import cn.zx.cang.ai.chain.domain.request.ChainRequest;
import cn.zx.cang.ai.chain.domain.response.ChainResponse;

public interface ChainService {
    /**
     * 创建交易链地址
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request);

    /**
     * 查询上链交易结果
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest request);

}
