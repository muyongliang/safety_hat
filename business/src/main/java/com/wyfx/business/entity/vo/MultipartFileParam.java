package com.wyfx.business.entity.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author johnson liu
 * @date 2019/12/3
 * @description 文件分片
 */
public class MultipartFileParam {

    /**
     * 文件MD5值
     */
    private String md5;

    private int chunks;//总分片数量

    private int chunk;//当前为第几块分片

    private long size = 0L;//当前分片大小

    private String fileName;//文件名

    private MultipartFile file;//分片对象

    private String latitude;//纬度
    private String longitude;//经度

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MultipartFileParam{" +
                "md5='" + md5 + '\'' +
                ", chunks=" + chunks +
                ", chunk=" + chunk +
                ", size=" + size +
                ", fileName='" + fileName + '\'' +
                ", file=" + file +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
