package com.wyfx.total.controller;


import com.wyfx.total.exception.*;
import com.wyfx.total.utile.RespBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.UUID;


/**
 * 1创建异常处理基类，让所有控制器继承此基类 ，  可以处理比如getUUID()等
 * 2通过aop注解@RestControllerAdvice标注该基类，不需要继承类就可以实现
 * 总结 创建一个异常处理基类，通过aop注解@RestControllerAdvice标注该基类，该类只处理异常。
 * 然后重新创建一个控制器基类来处理getUUID()等问题
 */
public abstract class BaseController {

    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //企业后台地址及端口
    @Value("${businessRemoteUrl}")
    protected String businessRemoteUrl;

    /**
     * 生成uuid
     *
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    //处理自定义异常类MyException
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public RespBean handleException(Exception e) {

        if (e instanceof BusinessNameConflictException) {
            return new RespBean(202, e.getMessage());
        } else if (e instanceof MainAccountConflictException) {
            return new RespBean(203, e.getMessage());
        } else if (e instanceof MainAccountFormatNotMatchException) {
            return new RespBean(204, e.getMessage());
        } else if (e instanceof UpdateDataException) {
            return new RespBean(205, e.getMessage());
        } else if (e instanceof DateFormatException) {
            return new RespBean(206, e.getMessage());
        } else if (e instanceof InsertDataException) {
            return new RespBean(207, e.getMessage());
        } else if (e instanceof DataNotAllowedToOperationException) {
            return new RespBean(208, e.getMessage());
        } else if (e instanceof UploadFileContentTypeException) {
            return new RespBean(209, e.getMessage());
        } else if (e instanceof NotHavePageSettingDataException) {
            return new RespBean(210, e.getMessage());
        } else if (e instanceof NotHaveDefaultSettingException) {
            return new RespBean(211, e.getMessage());
        } else if (e instanceof BusinessMessagesIsNotExistException) {
            return new RespBean(212, e.getMessage());
        } else if (e instanceof DictionariesNameConflictException) {
            return new RespBean(213, e.getMessage());
        } else if (e instanceof CanNotMaveException) {
            return new RespBean(214, e.getMessage());
        } else if (e instanceof BnameLengthTooLongException) {
            return new RespBean(215, e.getMessage());
        }
        return null;
    }


}
