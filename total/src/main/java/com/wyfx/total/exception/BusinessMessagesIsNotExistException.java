package com.wyfx.total.exception;

/**
 * 企业信息不存在
 */
public class BusinessMessagesIsNotExistException extends MyException {
    public BusinessMessagesIsNotExistException() {
    }

    public BusinessMessagesIsNotExistException(String message) {
        super(message);
    }

    public BusinessMessagesIsNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessMessagesIsNotExistException(Throwable cause) {
        super(cause);
    }

    public BusinessMessagesIsNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
