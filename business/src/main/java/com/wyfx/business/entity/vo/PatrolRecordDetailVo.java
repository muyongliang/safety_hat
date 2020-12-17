package com.wyfx.business.entity.vo;

/**
 * 巡检记录明细
 */
public class PatrolRecordDetailVo {

    private String title;

    private String event;

    private String content;

    private String result;

    private String imgUrls;


    public PatrolRecordDetailVo() {
    }

    public PatrolRecordDetailVo(String title, String event, String content, String result, String imgUrls) {
        this.title = title;
        this.event = event;
        this.content = content;
        this.result = result;
        this.imgUrls = imgUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }
}
