package com.wyfx.total.exception;

//上传文件失败
public class UploadIOException extends MyException {

    public UploadIOException() {
    }

    public UploadIOException(String message) {
        super(message);
    }

    public UploadIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadIOException(Throwable cause) {
        super(cause);
    }

    public UploadIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
