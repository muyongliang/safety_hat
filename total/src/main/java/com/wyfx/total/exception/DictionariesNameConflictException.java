package com.wyfx.total.exception;

/**
 * 字典名重复
 */
public class DictionariesNameConflictException extends MyException {
    public DictionariesNameConflictException() {
    }

    public DictionariesNameConflictException(String message) {
        super(message);
    }

    public DictionariesNameConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictionariesNameConflictException(Throwable cause) {
        super(cause);
    }

    public DictionariesNameConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
