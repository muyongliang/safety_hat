package com.wyfx.total.exception;

//主账号格式错误
public class MainAccountFormatNotMatchException extends MyException {

    public MainAccountFormatNotMatchException() {
    }

    public MainAccountFormatNotMatchException(String message) {
        super(message);
    }

    public MainAccountFormatNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MainAccountFormatNotMatchException(Throwable cause) {
        super(cause);
    }

    public MainAccountFormatNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
