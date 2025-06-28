package cn.zx.cang.ai.stream.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息
 *
 * @author kinchou
 */
@Data
@Accessors(chain = true)
public class Message {
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 消息体
     */
    private String body;
}