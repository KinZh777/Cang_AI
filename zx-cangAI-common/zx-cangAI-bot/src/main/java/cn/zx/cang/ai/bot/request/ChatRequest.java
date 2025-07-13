package cn.zx.cang.ai.bot.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String message;
    private String conversationId;
    private Long userId;
    private String botId;

    public static ChatRequest build(String message,String conversationId, Long userId, String botId) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage(message);
        chatRequest.setUserId(userId);
        chatRequest.setConversationId(conversationId);
        chatRequest.setBotId(botId);
        return chatRequest;
    }
}
