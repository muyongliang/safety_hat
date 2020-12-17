package com.wyfx.total.exception;

//时间格式不合理
public class DateFormatException extends MyException {

    public DateFormatException() {
    }

    public DateFormatException(String message) {
        super(message);
    }

    public DateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateFormatException(Throwable cause) {
        super(cause);
    }

    public DateFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
