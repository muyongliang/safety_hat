package com.wyfx.total.exception;

//用户名不匹配
public class UserNameNotMatchException extends MyException {

    public UserNameNotMatchException() {
    }

    public UserNameNotMatchException(String message) {
        super(message);
    }

    public UserNameNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameNotMatchException(Throwable cause) {
        super(cause);
    }

    public UserNameNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
