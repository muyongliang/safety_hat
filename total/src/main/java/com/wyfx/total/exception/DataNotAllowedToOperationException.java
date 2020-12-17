package com.wyfx.total.exception;

//数据不允许被操作
public class DataNotAllowedToOperationException extends MyException {
    public DataNotAllowedToOperationException() {
    }

    public DataNotAllowedToOperationException(String message) {
        super(message);
    }

    public DataNotAllowedToOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotAllowedToOperationException(Throwable cause) {
        super(cause);
    }

    public DataNotAllowedToOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
