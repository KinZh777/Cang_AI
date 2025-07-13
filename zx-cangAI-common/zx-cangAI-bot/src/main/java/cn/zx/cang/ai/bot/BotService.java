package cn.zx.cang.ai.bot;

import cn.zx.cang.ai.bot.request.ChatRequest;
import com.coze.openapi.client.auth.OAuthToken;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface BotService {
    public void creatCozeApi();
    String getConversationId(String botId);
    void streamWithCoze(ChatRequest request, SseEmitter emitter);
}
