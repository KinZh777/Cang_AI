package cn.zx.cang.ai.bot;

import cn.zx.cang.ai.bot.request.ChatRequest;
import com.coze.openapi.client.auth.OAuthToken;
import com.coze.openapi.client.chat.CreateChatReq;
import com.coze.openapi.client.chat.model.ChatEvent;
import com.coze.openapi.client.chat.model.ChatEventType;
import com.coze.openapi.client.connversations.CreateConversationReq;
import com.coze.openapi.client.connversations.CreateConversationResp;
import com.coze.openapi.client.connversations.message.CreateMessageReq;
import com.coze.openapi.client.connversations.message.CreateMessageResp;
import com.coze.openapi.client.connversations.message.model.Message;
import com.coze.openapi.client.connversations.message.model.MessageContentType;
import com.coze.openapi.client.connversations.message.model.MessageRole;
import com.coze.openapi.service.auth.JWTOAuthClient;
import com.coze.openapi.service.auth.TokenAuth;
import com.coze.openapi.service.service.CozeAPI;
import io.reactivex.Flowable;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import static cn.zx.cang.ai.bot.constant.CozeConstant.COZE_TOKEN_PREFIX;
import static com.coze.openapi.service.config.Consts.COZE_CN_BASE_URL;

@Slf4j
public class CozeServiceImpl implements BotService {

    @Value("classpath:private_key.pem")
    private Resource keyPem;

    @Setter
    @Getter
    private String clientID;

    @Setter
    @Getter
    private String publicKey;

    @Autowired
    private RedissonClient redissonClient;

    private CozeAPI cozeAPI;

    @PostConstruct
    @Override
    public void creatCozeApi(){
        OAuthToken oAuthToken =null;
        //项目启动时，获取accessToken
        try {
            // 获取私钥文件
            if(!keyPem.exists()) throw new Exception("请配置访问密钥");
            String jwtOauthPrivateKey = Files.readString(keyPem.getFile().toPath(), StandardCharsets.UTF_8);
            JWTOAuthClient oauth = new JWTOAuthClient.JWTOAuthBuilder()
                    .clientID(clientID)
                    .privateKey(jwtOauthPrivateKey)
                    .publicKey(publicKey)
                    .baseURL(COZE_CN_BASE_URL)
                    .ttl(86399)
                    .build();
            // 获取token
            oAuthToken = oauth.getAccessToken();
            if(oAuthToken==null)throw new Exception("请求失败");
        } catch (Exception e) {
            log.error("获取coze JWT token异常！${}", e.getMessage());
        }
        redissonClient.getBucket(COZE_TOKEN_PREFIX).set(oAuthToken);
        cozeAPI = new CozeAPI.Builder()
                .auth(new TokenAuth(oAuthToken.getAccessToken()))
                .baseURL(COZE_CN_BASE_URL)
                .readTimeout(10000)
                .build();
    }
    /**
     * JWT鉴权token
     */


    @Override
    public String createConversationId(String botId){
        CreateConversationResp resp = cozeAPI.conversations().create(new CreateConversationReq(null, null, botId));
        return resp.getConversation().getId();
    }

    @Override
    public void streamWithCoze(ChatRequest request, SseEmitter emitter) {
        //创建消息对象
        CreateMessageReq.CreateMessageReqBuilder<?, ?> msgReq = CreateMessageReq.builder()
                .conversationID(request.getConversationId())
                .role(MessageRole.USER)
                .content(request.getMessage())
                .contentType(MessageContentType.TEXT);
        CreateMessageResp msgResp = cozeAPI.conversations().messages().create(msgReq.build());
        Message msg = msgResp.getMessage();

        //开始流式对话
        CreateChatReq chatReq = CreateChatReq.builder()
                .stream(true)
                .autoSaveHistory(true)
                .botID(request.getBotId())
                .conversationID(msg.getConversationId())
                .userID(String.valueOf(request.getUserId()))
                .messages(Collections.singletonList(msg))
                .build();
        cozeAPI.chat().stream(chatReq).subscribe(
                event -> {
                    if (ChatEventType.CONVERSATION_MESSAGE_DELTA.equals(event.getEvent())) {
                        String content = event.getMessage().getContent();
                        try {
                            emitter.send(SseEmitter.event().data(content));
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    }
                },
                error -> {
                    emitter.completeWithError(error);
                },
                emitter::complete
        );;
    }
}
