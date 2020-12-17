package com.wyfx.total.exception;

/**
 * 没有默认页面设置
 */
public class NotHavePageSettingDataException extends MyException {
    public NotHavePageSettingDataException() {
    }

    public NotHavePageSettingDataException(String message) {
        super(message);
    }

    public NotHavePageSettingDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotHavePageSettingDataException(Throwable cause) {
        super(cause);
    }

    public NotHavePageSettingDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
