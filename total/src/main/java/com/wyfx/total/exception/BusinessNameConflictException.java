package com.wyfx.total.exception;

//企业名字冲突
public class BusinessNameConflictException extends MyException {

    public BusinessNameConflictException() {
    }

    public BusinessNameConflictException(String message) {
        super(message);
    }

    public BusinessNameConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessNameConflictException(Throwable cause) {
        super(cause);
    }

    public BusinessNameConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
