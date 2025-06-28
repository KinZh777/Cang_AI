package cn.zx.cang.ai.admin.infrastructure.exception;

import cn.zx.cang.ai.base.exception.ErrorCode;

/**
 * 后台错误码
 *
 * @author kinchou
 */
public enum AdminErrorCode implements ErrorCode {

    /**
     * 后台上传图片失败
     */
    ADMIN_USER_NOT_EXIST("ADMIN_USER_NOT_EXIST", "后台用户不存在"),
    USER_NOT_LOGIN("USER_NOT_LOGIN", "后台用户未登录");


    private String code;

    private String message;

    AdminErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
