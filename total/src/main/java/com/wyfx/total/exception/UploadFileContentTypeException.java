package com.wyfx.total.exception;

//上传文件类型错误
public class UploadFileContentTypeException extends MyException {
    public UploadFileContentTypeException() {
    }

    public UploadFileContentTypeException(String message) {
        super(message);
    }

    public UploadFileContentTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileContentTypeException(Throwable cause) {
        super(cause);
    }

    public UploadFileContentTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
