package com.wyfx.business.controller.commons;

public enum ResponseCode {

    /**
     * 操作成功
     */
    SUCCESS(200),
    /**
     * 404路径错误
     */
    ERROR_PATH(404),
    /**
     * 系统内部异常
     */
    ERROR_SYS(500),

    /**
     * 会话超时，请重新登陆
     */
    ERROR_SESSION(50001),

    /**
     * 用户鉴权失败
     */
    ERROR_IDENTIFY(50002),


    /**
     * 用户重复登录
     */
    ERROR_RELOGING(50003),

    /**
     * 参数错误
     */
    ERROR_PARAM(50004),

    FAILD_UPLOAD(50005);

    private Integer value;

    ResponseCode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
