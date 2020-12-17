package com.wyfx.business.alarmRange.api;

import com.wyfx.business.alarmRange.model.BaseRequest;

/**
 * @author johnson liu
 * @date 2019/11/29
 * @description 请求创建圆形围栏
 */
public class AddCirclefenceRequest extends BaseRequest {
    private String fence_name;
    private String monitored_person;
    private Double longitude;
    private Double latitude;
    private Double radius;
    private String coord_type;
    private String denoise;
    private String sn;

    public AddCirclefenceRequest() {
    }

    /**
     * @param ak        服务端AK
     * @param serviceId 轨迹服务ID
     */
    public AddCirclefenceRequest(String ak, long serviceId, String fence_name, String monitored_person, Double longitude, Double latitude, Double radius, String coord_type, String denoise, String sn) {
        super(ak, serviceId);
        this.fence_name = fence_name;
        this.monitored_person = monitored_person;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.coord_type = coord_type;
        this.denoise = denoise;
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "AddCirclefenceRequest{" +
                "fence_name='" + fence_name + '\'' +
                ", monitored_person='" + monitored_person + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", radius='" + radius + '\'' +
                ", coord_type='" + coord_type + '\'' +
                ", denoise='" + denoise + '\'' +
                ", sn='" + sn + '\'' +
                '}';
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
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
