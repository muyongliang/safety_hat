package com.wyfx.business.entity;

public class DefaultClientVideo {
    private Long vid;

    private Long businessId;

    private Integer resolution;

    private String resolutionStr;

    private Integer minKps;

    private Integer maxKps;

    public DefaultClientVideo(Long vid, Long businessId, Integer resolution, Integer minKps, Integer maxKps) {
        this.vid = vid;
        this.businessId = businessId;
        this.resolution = resolution;
        this.minKps = minKps;
        this.maxKps = maxKps;
    }

    public DefaultClientVideo() {
        super();
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Integer getMinKps() {
        return minKps;
    }

    public void setMinKps(Integer minKps) {
        this.minKps = minKps;
    }

    public Integer getMaxKps() {
        return maxKps;
    }

    public void setMaxKps(Integer maxKps) {
        this.maxKps = maxKps;
    }

    public String getResolutionStr() {
        return resolutionStr;
    }

    public void setResolutionStr(String resolutionStr) {
        this.resolutionStr = resolutionStr;
    }
}