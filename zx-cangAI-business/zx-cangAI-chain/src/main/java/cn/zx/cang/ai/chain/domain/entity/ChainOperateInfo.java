package cn.zx.cang.ai.chain.domain.entity;

import cn.zx.cang.ai.chain.domain.constant.ChainOperateStateEnum;
import cn.zx.cang.ai.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ChainOperateInfo extends BaseEntity {

    /**
     * 链类型
     */
    private String chainType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 业务类型
     *
     * @see cn.zx.cang.ai.api.chain.constant.ChainOperateBizTypeEnum
     */
    private String bizType;

    /**
     * 操作类型
     * @see cn.zx.cang.ai.api.chain.constant.ChainOperateTypeEnum
     */
    private String operateType;

    /**
     * 状态
     */
    private ChainOperateStateEnum state;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 成功时间
     */
    private Date succeedTime;

    /**
     * 入参
     */
    private String param;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 外部业务id
     */
    private String outBizId;


}
