package cn.zx.cang.ai.api.user.response.data;

import cn.zx.cang.ai.api.user.constant.UserRole;
import cn.zx.cang.ai.api.user.constant.UserStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author kinchou
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private String state;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;

    /**
     * 区块链地址
     */
    private String blockChainUrl;

    /**
     * 区块链平台
     */
    private String blockChainPlatform;

    /**
     * 实名认证
     */
    private Boolean certification;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 邀请码
     */
    private String inviteCode;
}
