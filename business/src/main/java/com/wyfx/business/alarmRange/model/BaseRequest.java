package com.wyfx.business.alarmRange.model;

/**
 * 基础请求信息类
 *
 * @author baidu
 */
public abstract class BaseRequest {

    /**
     * 服务端ak
     */
    private String ak;

    /**
     * 轨迹服务ID
     */
    private long serviceId;

    public BaseRequest() {
        super();
    }

    /**
     * @param ak        服务端AK
     * @param serviceId 轨迹服务ID
     */
    public BaseRequest(String ak, long serviceId) {
        super();
        this.ak = ak;
        this.serviceId = serviceId;
    }

    /**
     * 获取服务端AK
     *
     * @return
     */
    public String getAk() {
        return ak;
    }

    /**
     * 设置服务端AK
     *
     * @param ak
     */
    public void setAk(String ak) {
        this.ak = ak;
    }

    /**
     * 获取轨迹服务ID
     *
     * @return
     */
    public long getServiceId() {
        return serviceId;
    }

    /**
     * 设置轨迹服务ID
     *
     * @param serviceId
     */
    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "ak='" + ak + '\'' +
                ", serviceId=" + serviceId +
                '}';
    }
}
