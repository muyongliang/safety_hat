package com.wyfx.total.utile;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 响应对象
 *
 * @param <T>
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RespBean<T> implements Serializable {

    private int code;

    private T data;


    public RespBean() {
    }

    public RespBean(int code) {
        this.code = code;
    }

    public RespBean(int code, T data) {
        this.code = code;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
