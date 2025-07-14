package cn.zx.cang.ai.api.chain.request;

import cn.zx.cang.ai.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 链处理参数
 * @author kinchou
 * @date 2025/01/17
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChainProcessRequest extends BaseRequest {

    /**
     *
     * 链类型
     */
    private String chainType;
    /**
     * 幂等号
     */
    private String identifier;

    /**
     * 藏品类别ID
     */
    private String classId;

    /**
     * 藏品类别名称
     */
    private String className;

    /**
     * 藏品序列号
     */
    private String serialNo;

    /**
     * 接收者地址
     */
    private String recipient;

    /**
     * 持有者地址
     */
    private String owner;

    /**
     * ntf唯一id
     */
    private String ntfId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 业务id
     */
    @NotNull
    private String bizId;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    private String bizType;


}
