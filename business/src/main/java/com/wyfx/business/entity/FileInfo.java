package com.wyfx.business.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * update by wsm on 2019-12-17
 */
@Data
public class FileInfo {
    private Long fid;

    @Excel(name = "文件名", orderNum = "0")
    private String fileName;

    @Excel(name = "上传人账号", orderNum = "2")
    private String uploadByAccount;

    @Excel(name = "上传人姓名", orderNum = "1")
    private String uploadByName;

    @Excel(name = "账号类型", orderNum = "3", replace = {"0_企业管理员", "1_调度员", "2_终端账号", "3_其他管理员账号"})
//账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}
    private Integer userType;

    @Excel(name = "文件大小", orderNum = "4")
    private Long fileSize;
    @Excel(name = "视频时长", orderNum = "5")
    private Long duration;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "上传时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    private Integer type;

    private Long projectId;

    private String url;

    private String fileSizeStr;

    public FileInfo(Long fid, String fileName, String uploadByAccount, String uploadByName, Integer userType, Long fileSize, Long duration, Date uploadTime, Integer type, Long projectId, String url) {
        this.fid = fid;
        this.fileName = fileName;
        this.uploadByAccount = uploadByAccount;
        this.uploadByName = uploadByName;
        this.userType = userType;
        this.fileSize = fileSize;
        this.duration = duration;
        this.uploadTime = uploadTime;
        this.type = type;
        this.projectId = projectId;
        this.url = url;
    }
}