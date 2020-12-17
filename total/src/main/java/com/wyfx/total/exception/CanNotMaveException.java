package com.wyfx.total.exception;

public class CanNotMaveException extends MyException {
    public CanNotMaveException() {
    }

    public CanNotMaveException(String message) {
        super(message);
    }

    public CanNotMaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotMaveException(Throwable cause) {
        super(cause);
    }

    public CanNotMaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
