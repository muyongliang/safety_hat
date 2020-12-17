package com.wyfx.total.exception;


import java.io.Serializable;

/**
 * 自定义异常类 目前只处理运行时异常
 */
public class MyException extends RuntimeException implements Serializable {


    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
