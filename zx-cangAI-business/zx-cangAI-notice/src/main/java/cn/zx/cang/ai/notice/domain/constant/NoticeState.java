package cn.zx.cang.ai.notice.domain.constant;

/**
 * @author kinchou
 */
public enum NoticeState {
    /**
     * 初始化
     */
    INIT,

    /**
     * 已发送成功
     */
    SUCCESS,

    /**
     * 发送失败
     */
    FAILED,

    /**
     * 已挂起
     */
    SUSPENDED;
}
