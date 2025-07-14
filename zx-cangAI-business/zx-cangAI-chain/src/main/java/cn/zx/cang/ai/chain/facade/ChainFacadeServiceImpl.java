package cn.zx.cang.ai.chain.facade;

import cn.zx.cang.ai.api.chain.constant.ChainType;
import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.response.data.ChainOperationData;
import cn.zx.cang.ai.api.chain.service.ChainFacadeService;
import cn.zx.cang.ai.chain.domain.service.ChainService;
import cn.zx.cang.ai.chain.domain.service.ChainServiceFactory;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static cn.zx.cang.ai.base.constant.ProfileConstant.PROFILE_DEV;

@DubboService(version = "1.0.0")
public class ChainFacadeServiceImpl implements ChainFacadeService {

    @Value("${cang.ai.chain.type:MOCK}")
    private String chainType;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private ChainServiceFactory chainServiceFactory;

    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request) {
        return getChainService().createAddr(request);
    }

    @Override
    public ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest request) {
        return null;
    }

    @Override
    public ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request) {
        return null;
    }

    @Override
    public ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request) {
        return null;
    }

    @Override
    public ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request) {
        return null;
    }

    private ChainService getChainService() {
        if (PROFILE_DEV.equals(profile)) {
            return chainServiceFactory.get(ChainType.MOCK);
        }

        ChainService chainService = chainServiceFactory.get(ChainType.valueOf(chainType));
        return chainService;
    }
}
