package com.wyfx.total.exception;

//跟新数据异常
public class UpdateDataException extends MyException {
    public UpdateDataException() {
    }

    public UpdateDataException(String message) {
        super(message);
    }

    public UpdateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateDataException(Throwable cause) {
        super(cause);
    }

    public UpdateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
