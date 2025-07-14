package cn.zx.cang.ai.chain.domain.service;

import cn.zx.cang.ai.api.chain.constant.ChainOperateBizTypeEnum;
import cn.zx.cang.ai.api.chain.constant.ChainOperateTypeEnum;
import cn.zx.cang.ai.api.chain.constant.ChainType;
import cn.zx.cang.ai.api.chain.model.ChainOperateBody;
import cn.zx.cang.ai.api.chain.request.ChainProcessRequest;
import cn.zx.cang.ai.api.chain.request.ChainQueryRequest;
import cn.zx.cang.ai.api.chain.response.ChainProcessResponse;
import cn.zx.cang.ai.api.chain.response.data.ChainCreateData;
import cn.zx.cang.ai.api.chain.response.data.ChainOperationData;
import cn.zx.cang.ai.api.chain.response.data.ChainResultData;
import cn.zx.cang.ai.base.exception.RepoErrorCode;
import cn.zx.cang.ai.base.exception.SystemException;
import cn.zx.cang.ai.base.utils.BeanValidator;
import cn.zx.cang.ai.chain.domain.constant.ChainCodeEnum;
import cn.zx.cang.ai.chain.domain.constant.ChainOperateStateEnum;
import cn.zx.cang.ai.chain.domain.entity.ChainOperateInfo;
import cn.zx.cang.ai.chain.domain.request.ChainRequest;
import cn.zx.cang.ai.chain.domain.response.ChainResponse;
import cn.zx.cang.ai.limiter.SlidingWindowRateLimiter;
import cn.zx.cang.ai.stream.producer.StreamProducer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Slf4j
public abstract class AbstractChainService implements ChainService{

    @Autowired
    protected SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private ChainOperateInfoService chainOperateInfoService;

    @Autowired
    private StreamProducer streamProducer;

    private static ThreadFactory chainResultProcessFactory = new ThreadFactoryBuilder()
            .setNameFormat("chain-result-process-%d").build();

    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(10, chainResultProcessFactory);

    /**
     * 参数校验
     * @param request
     * @param function
     * @return
     * @param <T>
     * @param <R>
     */
    private static <T, R extends ChainProcessResponse> ChainProcessResponse handle(T request, Function<T,R> function){
        requireNonNull(request);
        BeanValidator.validateObject(request);
        return function.apply(request);
    }

    /**
     * 链操作通用流程
     * @param chainProcessRequest
     * @param chainOperateTypeEnum
     * @param consumer
     * @return
     */
    protected ChainProcessResponse doPostExecute(ChainProcessRequest chainProcessRequest, ChainOperateTypeEnum chainOperateTypeEnum,
                                                 Consumer<ChainRequest> consumer) {

        return handle(chainProcessRequest, request ->{
            //设置限流器 一分钟内允许通过一次
            Boolean rateLimitResult = slidingWindowRateLimiter.tryAcquire(
                    "limit#" + chainProcessRequest.getBizType()+"_"+ chainProcessRequest.getIdentifier(), 1, 60);
            if (!rateLimitResult) {
                return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.PROCESSING.name()).data(
                        new ChainOperationData(chainProcessRequest.getIdentifier())).buildSuccess();
            }
            //1. 查询链操作信息
            ChainOperateInfo chainOperateInfo = chainOperateInfoService.queryByOutBizId(chainProcessRequest.getBizId(), chainProcessRequest.getBizType(),
                    chainProcessRequest.getIdentifier());
            // 如果查到了此次链操作信息
            if (null != chainOperateInfo) {
                //根据链操作类型返回不同的链操作状态给前端
                return duplicateResponse(chainProcessRequest, chainOperateInfo);
            }
            //如果没有查到
            chainOperateInfo = new ChainOperateInfo();
            chainOperateInfo.setState(ChainOperateStateEnum.PROCESSING);
            chainOperateInfo.setOperateTime(new Date());
            chainOperateInfo.setChainType(chainOperateInfo.getChainType());
            chainOperateInfo.setBizId(chainOperateInfo.getBizId());
            chainOperateInfo.setBizType(chainOperateInfo.getBizType());
            chainOperateInfo.setOperateType(chainOperateInfo.getOperateType());
            chainOperateInfo.setParam(chainOperateInfo.getParam());
            chainOperateInfo.setOutBizId(chainOperateInfo.getOutBizId());
            boolean save = chainOperateInfoService.save(chainOperateInfo);
            if(!save){
                throw new SystemException(RepoErrorCode.INSERT_FAILED);
            }
            Long operateInfoId = chainOperateInfo.getId();
            //构建请求参数
            ChainRequest chainRequest = new ChainRequest();
            consumer.accept(chainRequest);
            //向链服务器发送链操作请求
            ChainResponse result = doPost(chainRequest);
            log.info("post result:{}", JSON.toJSONString(result));
            //获取请求结果，更新到数据库
            chainOperateInfo.setResult(result.getSuccess()?result.getData().toString():result.getError().toString());
            boolean updateResult = chainOperateInfoService.updateById(chainOperateInfo);
            if(!updateResult){
                throw new SystemException(RepoErrorCode.UPDATE_FAILED);
            }
            // 根据链请求结果,返回不同响应
            ChainProcessResponse response = buildResult(result, chainProcessRequest, chainOperateTypeEnum);
            //如果此次结果成功且链操作不是创建链用户
            if (response.getSuccess() && chainOperateTypeEnum != ChainOperateTypeEnum.USER_CREATE) {
                //延迟5秒钟之后查询状态并发送 MQ 消息通知上游
                scheduler.schedule(() -> {
                    try {
                        //从数据库查询此次链请求
                        ChainOperateInfo operateInfo = chainOperateInfoService.queryByOutBizId(chainProcessRequest.getBizId(), chainProcessRequest.getBizType(),
                                chainProcessRequest.getIdentifier());
                        //去链上查询此次链请求结果
                        ChainProcessResponse<ChainResultData> queryChainResult = queryChainResult(
                                new ChainQueryRequest(chainProcessRequest.getIdentifier(), operateInfoId.toString()));
                        if (queryChainResult.getSuccess() && queryChainResult.getData() != null) {
                            if (StringUtils.equals(queryChainResult.getData().getState(), ChainOperateStateEnum.SUCCEED.name())) {
                                chainOperateInfoService.updateResult(operateInfoId,
                                        ChainOperateStateEnum.SUCCEED, null);
                                //消息队列异通知上游
                                this.sendMsg(operateInfo, queryChainResult.getData());
                            }
                        }
                    } catch (Exception e) {
                        log.error("query chain result failed,", e);
                    }
                }, 5, TimeUnit.SECONDS);
            }
            //如果请求失败或者是创建对象，直接返回结果
            return response;
        });
    }
    /**
     * 结果构造
     * @param result
     * @param chainProcessRequest
     * @param chainOperateTypeEnum
     * @return
     */
    private ChainProcessResponse buildResult(ChainResponse result, ChainProcessRequest chainProcessRequest, ChainOperateTypeEnum chainOperateTypeEnum) {

        if (result.getSuccess()) {
            if (chainOperateTypeEnum == ChainOperateTypeEnum.USER_CREATE) {
                JSONObject dataJsonObject = result.getData();
                String blockChainAddr = (String) dataJsonObject.get("native_address");
                String blockChainName = chainProcessRequest.getUserId();
                return new ChainProcessResponse.Builder().data(
                        new ChainCreateData(chainProcessRequest.getIdentifier(), blockChainAddr, blockChainName,
                                chainProcessRequest.getChainType())).buildSuccess();
            } else {
                return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.PROCESSING.name()).data(
                        new ChainOperationData(chainProcessRequest.getIdentifier())).buildSuccess();
            }
        }
        return new ChainProcessResponse.Builder().responseCode(result.getResponseCode()).responseMessage(
                result.getResponseMessage()).buildFailed();
    }

    /**
     * 根据链操作类型返回不同响应
     * @param chainProcessRequest
     * @param chainOperateInfo
     * @return
     */
    private ChainProcessResponse duplicateResponse(ChainProcessRequest chainProcessRequest, ChainOperateInfo chainOperateInfo) {
        if(StringUtils.equals(chainProcessRequest.getBizType(),ChainOperateTypeEnum.USER_CREATE.name())) {
            JSONObject jsonObject = JSON.parseObject(chainOperateInfo.getResult(), JSONObject.class);
            String blockChainAddr = (String) jsonObject.get("native_address");
            String blockChainName = chainProcessRequest.getUserId();
            return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.SUCCESS.name()).data(
                    new ChainCreateData(chainProcessRequest.getIdentifier(), blockChainName, blockChainAddr, chainOperateInfo.getChainType())).buildSuccess();
        }else
            return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.PROCESSING.name()).data(
                    new ChainOperationData(chainProcessRequest.getIdentifier())).buildSuccess();
    }

    /**
     * 发送异步消息
     * @param chainOperateInfo
     * @param chainResultData
     */
    protected void sendMsg(ChainOperateInfo chainOperateInfo, ChainResultData chainResultData){
        ChainOperateBody chainOperateBody = new ChainOperateBody();
        chainOperateBody.setBizId(chainOperateInfo.getBizId());
        chainOperateBody.setBizType(ChainOperateBizTypeEnum.valueOf(chainOperateInfo.getBizType()));
        chainOperateBody.setOperateInfoId(chainOperateInfo.getId());
        chainOperateBody.setOperateType(ChainOperateTypeEnum.valueOf(chainOperateInfo.getOperateType()));
        chainOperateBody.setChainType(ChainType.valueOf(chainOperateInfo.getChainType()));
        chainOperateBody.setChainResultData(chainResultData);
        //消息监听：ChainOperateResultListener
        streamProducer.send("chain-out-0", chainOperateInfo.getBizType(), JSON.toJSONString(chainOperateBody));
    }

    /**
     * 提供给子类实现，不同链服务post请求不同
     * @param chainRequest
     * @return
     */
    protected abstract ChainResponse doPost(ChainRequest chainRequest);

    /**
     *
     * @param chainRequest
     * @return
     */
    protected abstract ChainResponse doGetQuery(ChainRequest chainRequest);


}
