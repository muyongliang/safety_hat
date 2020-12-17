package com.wyfx.business.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author johnson liu
 * @date 2019/12/16
 * @description 采集信息
 */
@ApiModel
public class GpsVo {
    @ApiModelProperty("电量")
    private String power;
    @ApiModelProperty("主板温度")
    private String mainTemperature;
    @ApiModelProperty("环境温度")
    private String environmentTemperature;
    @ApiModelProperty("大气压")
    private String pressure;
    @ApiModelProperty("网络")
    private String network;
    @ApiModelProperty("经度")
    private Double longitude;
    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("湿度")
    private String humidity;

    public GpsVo() {
    }

    public GpsVo(String power, String mainTemperature, String environmentTemperature, String pressure, String network, Double longitude, Double latitude, String humidity) {
        this.power = power;
        this.mainTemperature = mainTemperature;
        this.environmentTemperature = environmentTemperature;
        this.pressure = pressure;
        this.network = network;
        this.longitude = longitude;
        this.latitude = latitude;
        this.humidity = humidity;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getMainTemperature() {
        return mainTemperature;
    }

    public void setMainTemperature(String mainTemperature) {
        this.mainTemperature = mainTemperature;
    }

    public String getEnvironmentTemperature() {
        return environmentTemperature;
    }

    public void setEnvironmentTemperature(String environmentTemperature) {
        this.environmentTemperature = environmentTemperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
