package cn.zx.cang.ai.chain.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChainRequest {
    /**
     * 签名
     */
    private String signature;

    /**
     * 域名
     */
    private String host;

    /**
     * 当前时间
     */
    private Long currentTime;

    /**
     * 请求体
     *  不同的链操作有不同的请求体
     */
    private RequestBody body;
    /**
     * 请求路径
     */
    private String path;

    public ChainRequest build(String signature, String host, Long currentTime, RequestBody body, String path){
        this.signature = signature;
        this.host = host;
        this.currentTime = currentTime;
        this.body = body;
        this.path = path;
        return this;
    }
}
