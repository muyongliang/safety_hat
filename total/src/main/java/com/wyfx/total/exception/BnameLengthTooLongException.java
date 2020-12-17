package com.wyfx.total.exception;

/**
 * 公司名超长
 */
public class BnameLengthTooLongException extends MyException {
    public BnameLengthTooLongException() {
    }

    public BnameLengthTooLongException(String message) {
        super(message);
    }

    public BnameLengthTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public BnameLengthTooLongException(Throwable cause) {
        super(cause);
    }

    public BnameLengthTooLongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
