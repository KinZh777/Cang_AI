package cn.zx.cang.ai.admin.infrastructure.exception;

import cn.zx.cang.ai.base.exception.BizException;
import cn.zx.cang.ai.base.exception.ErrorCode;

/**
 * 后台异常
 *
 * @author kinchou
 */
public class AdminException extends BizException {

    public AdminException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AdminException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AdminException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public AdminException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public AdminException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
