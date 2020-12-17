package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/12/30
 * @description 磁盘空间使用情况
 */
public class DiskSpaceVo {
    /**
     * 总共已用空间大小
     */
    private Long used = 0L;
    /**
     * 照片
     */
    private Long images = 0L;
    /**
     * 视频
     */
    private Long video = 0L;
    /**
     * 日志
     */
    private Long log = 0L;
    /**
     * 对讲记录
     */
    private Long talkBack = 0L;
    /**
     * 电话录音
     */
    private Long phoneRecord = 0L;
    /**
     * 广播记录录音
     */
    private Long broadcast = 0L;

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public Long getImages() {
        return images;
    }

    public void setImages(Long images) {
        this.images = images;
    }

    public Long getVideo() {
        return video;
    }

    public void setVideo(Long video) {
        this.video = video;
    }

    public Long getLog() {
        return log;
    }

    public void setLog(Long log) {
        this.log = log;
    }

    public Long getTalkBack() {
        return talkBack;
    }

    public void setTalkBack(Long talkBack) {
        this.talkBack = talkBack;
    }

    public Long getPhoneRecord() {
        return phoneRecord;
    }

    public void setPhoneRecord(Long phoneRecord) {
        this.phoneRecord = phoneRecord;
    }

    public Long getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Long broadcast) {
        this.broadcast = broadcast;
    }
}
