package com.wyfx.business.app.vo;

import com.wyfx.business.utils.DateUtil;

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/12/16
 * @description app通讯录Vo, 包含了最后一次定位信息
 */
public class ContactsForLocationVo extends ContactsVo {
    /**
     * 纬度
     */
    private Double latitude = 0.0;

    /**
     * 经度
     */
    private Double longitude = 0.0;
    /**
     * 最后一次定位时间
     */
    private Date lastLocationTime;

    private String lastLocationTimeStr = "";

    public ContactsForLocationVo() {

    }

    public ContactsForLocationVo(Integer id, String name, String fullName, String mobile, Integer status, Integer type, String workName, String color, Double latitude, Double longitude, Date lastLocationTime, String tname) {
        super(id, name, fullName, mobile, status, type, workName, color, tname);
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastLocationTime = lastLocationTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = (latitude == null) ? 0.0 : latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = (longitude == null) ? 0.0 : longitude;
    }

    public Date getLastLocationTime() {
        return lastLocationTime;
    }

    public void setLastLocationTime(Date lastLocationTime) {
        this.lastLocationTime = lastLocationTime;
        this.lastLocationTimeStr = (lastLocationTime == null) ? "" : DateUtil.convertTimeToStr(lastLocationTime);
    }

    public String getLastLocationTimeStr() {
        return lastLocationTimeStr;
    }

}
