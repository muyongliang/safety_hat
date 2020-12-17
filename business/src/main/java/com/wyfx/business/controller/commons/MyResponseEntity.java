package com.wyfx.business.controller.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对请求详细的结果进行重新封装，规范接口返回值
 *
 * @param <T>
 * @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
 */
/*@JsonInclude(value = JsonInclude.Include.NON_NULL)*/
@ApiModel(value = "结果实体类")
public class MyResponseEntity<T> {
    /**
     * 请求结果状态码
     */
    @ApiModelProperty(value = "请求结果状态码")
    private Integer code;

    /**
     * 提示信息
     */
    @ApiModelProperty(value = "提示信息")
    private String message;

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的数据")
    private T data;

    public MyResponseEntity(Integer code) {
        this.code = code;
    }

    public MyResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MyResponseEntity(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public MyResponseEntity(Integer code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }


    public MyResponseEntity(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
