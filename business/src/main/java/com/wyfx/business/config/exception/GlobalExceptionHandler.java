package com.wyfx.business.config.exception;

import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ParameterNameProvider;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public MyResponseEntity globalException(HttpServletResponse response, Exception ex) {
        MyResponseEntity result = null;
        ex.printStackTrace();
        if (ex instanceof NullPointerException) {
            result = new MyResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部异常");
        }
        if (ex instanceof ParameterNameProvider) {
            result = new MyResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "非法参数");
        }
        if (ex instanceof SafetyHatException) {
            result = new MyResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
        if (ex instanceof SafetyHatSessionException) {
            result = new MyResponseEntity(ResponseCode.ERROR_SESSION.getValue(), ex.getMessage());
        }
        if (ex instanceof NumberFormatException) {
            result = new MyResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "非法参数类型");
        }
        if (ex instanceof UnauthorizedException) {
            result = new MyResponseEntity(HttpStatus.UNAUTHORIZED.value(), "未授权");
        }
        return result;
    }


}
