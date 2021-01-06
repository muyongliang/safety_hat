package com.wyfx.total.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * create by wsm on 2019-12-12
 * 企业后台获取企业设置 vo类
 */
@Data
@AllArgsConstructor
public class DiySetVo {

    private String token;

    private Integer deviceNumLimit;//终端账号数量限制

    private String mainAccount;//企业的主账号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validityTime;//到期时间

    private Integer isCall;//是否支持语音通话 0支持，1不支持

    private Integer talkback;//是否支持对讲  0支持，1不支持

    private Integer vedio; //是否支持视频通话  0支持，1不支持

    private Integer broadcast; //是否支持广播  0支持，1不支持

    private Integer mapRange;//是否支持电子围栏  0支持，1不支持

    private Integer trail;//轨迹  0支持，1不支持

    private Integer listener;//无声侦护  0支持，1不支持

    private Integer vedioTimeLimit;//终端视频录制时长限制(单位:分钟)


    private Integer isAutoUpload;//终端视频是否自动上传(单位:M)


    private Integer dispatcherUploadLimit;//调度员视频自动上传限制(单位:M)


    private Integer appLog;//终端日志记录开关(几天后自动清理日志)


    private Integer dispatcherLog;//调度员日志记录开关(几天后自动清理日志)


    private Integer storeTip;//企业存储空间报警设置


}
