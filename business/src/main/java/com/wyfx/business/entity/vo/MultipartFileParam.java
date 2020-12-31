package com.wyfx.business.entity.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author johnson liu
 * @date 2019/12/3
 * @description 文件分片
 */
@Data
@ToString
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
    //    视频时长,只有当type=2时有效
    private Long duration;
}
