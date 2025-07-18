package cn.zx.cang.ai.chain.domain.service.impl;

import cn.hutool.core.lang.Assert;
import cn.zx.cang.ai.api.chain.constant.ChainOperateBizTypeEnum;
import cn.zx.cang.ai.api.chain.constant.ChainOperateTypeEnum;
import cn.zx.cang.ai.api.chain.constant.ChainType;
import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.request.ChainQueryRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.response.data.ChainResultData;
import cn.zx.cang.ai.base.exception.BizException;
import cn.zx.cang.ai.chain.domain.constant.ChainOperateStateEnum;
import cn.zx.cang.ai.chain.domain.entity.ChainOperateInfo;
import cn.zx.cang.ai.chain.domain.request.ChainRequest;
import cn.zx.cang.ai.chain.domain.response.ChainResponse;
import cn.zx.cang.ai.chain.domain.service.AbstractChainService;
import cn.zx.cang.ai.chain.domain.service.ChainOperateInfoService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service("mockChainService")
public class MockChainServiceImpl extends AbstractChainService {

    @Autowired
    private ChainOperateInfoService chainOperateInfoService;


    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request) {
        //使用用户id作为本次业务id
        request.setBizId(request.getUserId());
        request.setBizType(ChainOperateBizTypeEnum.USER.name());
        request.setChainType(ChainType.MOCK.name());
        ChainProcessResponse chainProcessResponse = doPostExecute(request, ChainOperateTypeEnum.USER_CREATE, chainRequest -> chainRequest.build("", "", System.currentTimeMillis(), null, ""));
        //如果返回成功，更新链操作为成功状态
        if (chainProcessResponse.getSuccess() && chainProcessResponse.getData() != null) {
            ChainOperateInfo chainOperateInfo = chainOperateInfoService.queryByOutBizId(request.getBizId(), request.getBizType(),
                    request.getIdentifier());
            boolean updateResult = chainOperateInfoService.updateResult(chainOperateInfo.getId(),
                    ChainOperateStateEnum.SUCCEED,
                    null);
        }
        return chainProcessResponse;
    }

    @Override
    public ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest request) {
        Long chainOperateInfoId = chainOperateInfoService.insertInfo(ChainType.MOCK.name(), request.getOperationInfoId(), ChainOperateBizTypeEnum.CHAIN_OPERATION.name(), ChainOperateTypeEnum.COLLECTION_QUERY.name(),
                JSON.toJSONString(request), request.getOperationId());
        ChainRequest chainRequest = new ChainRequest();
        chainRequest.build(null, "", System.currentTimeMillis(), null, "");
        ChainResponse result = doGetQuery(chainRequest);
        log.info("wen chang query result:{}", result);

        boolean updateResult = chainOperateInfoService.updateResult(chainOperateInfoId,
                ChainOperateStateEnum.SUCCEED,
                result.getSuccess() ? result.getData().toString() : result.getError().toString());
        ChainProcessResponse<ChainResultData> response = new ChainProcessResponse<>();
        response.setSuccess(result.getSuccess());
        response.setResponseMessage(result.getResponseMessage());
        if(result.getSuccess()){
            response.setResponseCode("200");
            response.setResponseMessage("SUCCESS");
            ChainResultData data = new ChainResultData();
            data.setTxHash(UUID.randomUUID().toString());
            data.setNftId("test");
            data.setState(ChainOperateStateEnum.SUCCEED.name());
            response.setData(data);
        }else {
            response.setResponseCode("404");
            response.setResponseMessage("FAILED");
            ChainResultData data = new ChainResultData();
            data.setTxHash(UUID.randomUUID().toString());
            data.setNftId("test");
            data.setState(ChainOperateStateEnum.FAILED.name());
            response.setData(data);
        }
        return response;
    }


    @Override
    protected ChainResponse doPost(ChainRequest chainRequest) {
        return null;
    }

    @Override
    public ChainResponse doGetQuery(ChainRequest chainRequest) {
        Random random = new Random();
        ChainResponse chainResponse = new ChainResponse();
        if(random.nextInt(100)==99){
            chainResponse.setSuccess(false);
            JSONObject data = new JSONObject();
            data.put("success",false);
            data.put("chainType","mock");
            chainResponse.setResponseMessage("创建失败");
            chainResponse.setData(data);
            return chainResponse;
        }
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setResponseMessage("创建成功");
        chainResponse.setData(data);
        return chainResponse;

    }
}
