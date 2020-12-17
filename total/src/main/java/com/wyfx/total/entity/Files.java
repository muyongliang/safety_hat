package com.wyfx.total.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;


//test  excel导入导出类
@Deprecated
public class Files implements Serializable {
    @Excel(name = "姓名", orderNum = "0")
    public String fileId;
    @Excel(name = "性别", replace = {"男_1", "女_2"}, orderNum = "1")
    public String fileName;
    @Excel(name = "别名", orderNum = "2")
    public String tags;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}