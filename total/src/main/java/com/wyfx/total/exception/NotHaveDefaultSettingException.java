package com.wyfx.total.exception;

/**
 * 未添加系统默认设置
 */
public class NotHaveDefaultSettingException extends MyException {
    public NotHaveDefaultSettingException() {
    }

    public NotHaveDefaultSettingException(String message) {
        super(message);
    }

    public NotHaveDefaultSettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotHaveDefaultSettingException(Throwable cause) {
        super(cause);
    }

    public NotHaveDefaultSettingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
