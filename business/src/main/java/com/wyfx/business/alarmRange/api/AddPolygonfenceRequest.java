package com.wyfx.business.alarmRange.api;

import com.wyfx.business.alarmRange.model.BaseRequest;

/**
 * @author johnson liu
 * @date 2019/11/30
 * @description 多边形围栏
 */
public class AddPolygonfenceRequest extends BaseRequest {
    /**
     * 围栏名称
     */
    private String fence_name;
    /**
     * 监控对象
     */
    private String monitored_person;
    /**
     * 多边形围栏形状点
     */
    private String vertexes;
    /**
     * 坐标类型
     */
    private String coord_type;
    /**
     * 围栏去噪参数
     */
    private String denoise;
    /**
     * 用户的权限签名
     */
    private String sn;

    public AddPolygonfenceRequest() {

    }

    /**
     * @param ak        服务端AK
     * @param serviceId 轨迹服务ID
     */
    public AddPolygonfenceRequest(String ak, long serviceId, String fence_name, String monitored_person, String vertexes, String coord_type, String denoise, String sn) {
        super(ak, serviceId);
        this.fence_name = fence_name;
        this.monitored_person = monitored_person;
        this.vertexes = vertexes;
        this.coord_type = coord_type;
        this.denoise = denoise;
        this.sn = sn;
    }

    public String getFence_name() {
        return fence_name;
    }

    public void setFence_name(String fence_name) {
        this.fence_name = fence_name;
    }

    public String getMonitored_person() {
        return monitored_person;
    }

    public void setMonitored_person(String monitored_person) {
        this.monitored_person = monitored_person;
    }

    public String getVertexes() {
        return vertexes;
    }

    public void setVertexes(String vertexes) {
        this.vertexes = vertexes;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(String coord_type) {
        this.coord_type = coord_type;
    }

    public String getDenoise() {
        return denoise;
    }

    public void setDenoise(String denoise) {
        this.denoise = denoise;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
