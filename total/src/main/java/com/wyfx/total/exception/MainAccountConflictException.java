package com.wyfx.total.exception;

//主账号冲突
public class MainAccountConflictException extends MyException {
    public MainAccountConflictException() {
    }

    public MainAccountConflictException(String message) {
        super(message);
    }

    public MainAccountConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public MainAccountConflictException(Throwable cause) {
        super(cause);
    }

    public MainAccountConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
