package cn.zx.cang.ai.collection.controller;

import cn.hutool.core.lang.Assert;
import cn.zx.cang.ai.api.collection.model.CollectionVO;
import cn.zx.cang.ai.api.collection.model.HeldCollectionVO;
import cn.zx.cang.ai.base.response.SingleResponse;
import cn.zx.cang.ai.bot.token.BotToken;
import cn.zx.cang.ai.collection.domain.vo.CollectionVo;
import cn.zx.cang.ai.api.collection.response.CollectionChatResponse;
import cn.zx.cang.ai.bot.BotService;
import cn.zx.cang.ai.bot.request.ChatRequest;
import cn.zx.cang.ai.collection.domain.entity.Collection;
import cn.zx.cang.ai.collection.domain.service.CollectionService;
import cn.zx.cang.ai.collection.infrastructure.CollectionMapper;
import cn.zx.cang.ai.collection.param.CollectionChatParam;
import cn.zx.cang.ai.collection.param.TransferParam;
import cn.zx.cang.ai.web.vo.MultiResult;
import cn.zx.cang.ai.web.vo.Result;
import com.coze.openapi.client.auth.OAuthToken;
import com.coze.openapi.client.connversations.CreateConversationReq;
import com.coze.openapi.client.connversations.CreateConversationResp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;

import static cn.zx.cang.ai.bot.constant.CozeConstant.COZE_CONVERSATION_SUFFIX;
import static cn.zx.cang.ai.bot.constant.CozeConstant.COZE_TOKEN_PREFIX;

@Slf4j
@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    private BotService botService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CollectionMapper collectionMapper;

    //TODO 获取藏品列表
    @PostMapping("/collectionList")
    public MultiResult<CollectionVO> collectionList(){
        return null;
    }

    //TODO 获取藏品信息
    @GetMapping("/collectionInfo")
    public Result<CollectionVO> collectionInfo(@NotBlank String collectionId) {
        return null;
    }

    //TODO 获取用户持有的藏品列表
    @GetMapping("/heldCollectionList")
    public MultiResult<HeldCollectionVO> heldCollectionList(String keyword, String state, int pageSize, int currentPage) {
        return null;
    }

    //TODO 获取用户持有的藏品信息
    @GetMapping("/heldCollectionInfo")
    public Result<HeldCollectionVO> heldCollectionInfo(@NotBlank String heldCollectionId) {
        //SingleResponse<HeldCollectionVO> singleResponse = collectionReadFacadeService.queryHeldCollectionById(Long.valueOf(heldCollectionId));
        //return Result.success(singleResponse.getData());
        return null;
    }

    @PostMapping("/destroy")
    public Result<Boolean> destroy(@NotBlank(message = "collectionId 不能为空") String heldCollectionId) {

        return null;
    }

    @PostMapping("/transfer")
    public Result<Boolean> transfer(@Valid @RequestBody TransferParam param) {
        return null;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @BotToken
    public SseEmitter chatWithCollection(@RequestBody CollectionChatParam collectionChatParam) throws Exception {
        SseEmitter emitter = new SseEmitter(0L);
        //1. 查询藏品
        Collection collection = collectionMapper.selectById(collectionChatParam.getCollectionId());
        Assert.isTrue(collection!=null, ()->new Exception("藏品不存在"));
        //2. 判断藏品是否有对接智能体
        Assert.isTrue(StringUtils.isNotBlank(collection.getBotId()), ()->new Exception("藏品不存在"));
        //3. 获取access Token;
        RBucket<OAuthToken> tokenBucket = redissonClient.getBucket(COZE_TOKEN_PREFIX);
        if(tokenBucket.isExists() && Instant.ofEpochSecond(tokenBucket.get().getExpiresIn()).isAfter(Instant.now())){
            botService.creatCozeApi();
        }
        String conversationId = (String) redissonClient.getBucket(COZE_CONVERSATION_SUFFIX + "1").get();
        // 开始流式对话
        ChatRequest chatRequest = ChatRequest.build(collectionChatParam.getMessage(),
                conversationId, collectionChatParam.getUserId(),collection.getBotId());
        botService.streamWithCoze(chatRequest, emitter);
        return emitter;
    }

    @GetMapping("/collectionInfo")
    public Result<CollectionVo> creatNewConversation(@NotBlank String collectionId) throws Exception {
        SseEmitter emitter = new SseEmitter(0L);
        //1. 查询藏品
        Collection collection = collectionMapper.selectById(collectionId);
        Assert.isTrue(collection!=null, ()->new Exception("藏品不存在"));
        CollectionVo collectionVo = new CollectionVo();
        //2. 判断藏品是否有对接智能体
        if(!StringUtils.isNotBlank(collection.getBotId())){
            collectionVo.setId(Long.valueOf(collectionId));
            collectionVo.setInfo("我不是一个智能体藏品");
            collectionVo.setBot(Boolean.FALSE);
            return Result.success(collectionVo);
        }
        //3. 获取access Token;
        RBucket<OAuthToken> tokenBucket = redissonClient.getBucket(COZE_TOKEN_PREFIX);
        if(tokenBucket.isExists() && Instant.ofEpochSecond(tokenBucket.get().getExpiresIn()).isAfter(Instant.now())){
            botService.creatCozeApi();
        }
        // 4.获取会话
        String conversationId = null;
        //fixme userID
        RBucket<String> conversationBucket = redissonClient.getBucket(COZE_CONVERSATION_SUFFIX + "1");
        if (conversationBucket.isExists()) {
            conversationId = conversationBucket.get();
        } else {
            //反之创建新会话
            conversationId = botService.createConversationId(collection.getBotId());
            conversationBucket.set(conversationId);
            conversationBucket.expire(Instant.now().plusSeconds(60));
        }
        //5.返回VO对象
        collectionVo.setId(Long.valueOf(conversationId));
        collectionVo.setInfo("可以和我对话啦");
        collectionVo.setBot(Boolean.TRUE);
        return Result.success(collectionVo);
    }
}
