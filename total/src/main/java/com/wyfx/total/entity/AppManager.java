package com.wyfx.total.entity;

import java.util.Date;

public class AppManager {
    private Long id;

    private String fileName;

    private String clientVersion;

    private String version;

    private String description;

    private Date ctime;

    private String creator;

    private Date uploadTime;

    private String url;

    private Double targetSize; //大小M
    private String newMd5;
    private Integer cons; //约束 0false 1 true

    public AppManager(Long id, String fileName, String clientVersion, String version, String description, Date ctime, String creator, Date uploadTime, String url, Double targetSize, String newMd5, Integer cons) {
        this.id = id;
        this.fileName = fileName;
        this.clientVersion = clientVersion;
        this.version = version;
        this.description = description;
        this.ctime = ctime;
        this.creator = creator;
        this.uploadTime = uploadTime;
        this.url = url;
        this.targetSize = targetSize;
        this.newMd5 = newMd5;
        this.cons = cons;
    }

    public AppManager() {
        super();
    }

    public Double getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(Double targetSize) {
        this.targetSize = targetSize;
    }

    public String getNewMd5() {
        return newMd5;
    }

    public void setNewMd5(String newMd5) {
        this.newMd5 = newMd5;
    }

    public Integer getCons() {
        return cons;
    }

    public void setCons(Integer cons) {
        this.cons = cons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion == null ? null : clientVersion.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }


    @Override
    public String toString() {
        return "AppManager{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", ctime=" + ctime +
                ", creator='" + creator + '\'' +
                ", uploadTime=" + uploadTime +
                ", url='" + url + '\'' +
                ", targetSize=" + targetSize +
                ", newMd5='" + newMd5 + '\'' +
                ", cons=" + cons +
                '}';
    }
}