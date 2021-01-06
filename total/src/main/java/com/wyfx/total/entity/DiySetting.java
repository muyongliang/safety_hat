package com.wyfx.total.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义设置
@ApiModel()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiySetting {

    @ApiModelProperty(hidden = true)
    private Long did;
    @ApiModelProperty(value = "是否支持语音通话 0支持，1不支持 ", name = "isCall", required = true)
    private Integer isCall;//是否支持语音通话 0支持，1不支持  以下一样
    @ApiModelProperty(value = "是否支持对讲 0支持，1不支持 ", name = "talkback", required = true)
    private Integer talkback;//是否支持对讲
    @ApiModelProperty(value = "是否支持视频通话 0支持，1不支持 ", name = "vedio", required = true)
    private Integer vedio; //是否支持视频通话
    @ApiModelProperty(value = "是否支持广播 0支持，1不支持 ", name = "broadcast", required = true)
    private Integer broadcast; //是否支持广播
    @ApiModelProperty(value = "是否支持电子围栏 0支持，1不支持 ", name = "mapRange", required = true)
    private Integer mapRange;//是否支持电子围栏
    @ApiModelProperty(value = "是否支持轨迹 0支持，1不支持 ", name = "trail", required = true)
    private Integer trail;//轨迹
    @ApiModelProperty(value = "是否支持无声侦护 0支持，1不支持 ", name = "listener", required = true)
    private Integer listener;//无声侦护

    @ApiModelProperty(hidden = true)
    private String bid;//企业uuID

    @ApiModelProperty(value = "终端视频录制时长限制(单位:分钟)", name = "vedioTimeLimit", required = true)
    private Integer vedioTimeLimit;//终端视频录制时长限制(单位:分钟)


    @ApiModelProperty(value = "终端视频是否自动上传(单位:M)", name = "isAutoUpload", required = true)
    private Integer isAutoUpload;//终端视频是否自动上传(单位:M)


    @ApiModelProperty(value = "调度员视频自动上传限制(单位:M)", name = "dispatcherUploadLimit", required = true)
    private Integer dispatcherUploadLimit;//调度员视频自动上传限制(单位:M)


    @ApiModelProperty(value = "终端日志记录开关(几天后自动清理日志)", name = "appLog", required = true)
    private Integer appLog;//终端日志记录开关(几天后自动清理日志)   todo 2限制才有值，其他两项都为null(即前端不用传该参数)


    @ApiModelProperty(value = "调度员日志记录开关(几天后自动清理日志)", name = "dispatcherLog", required = true)
    private Integer dispatcherLog;//调度员日志记录开关(几天后自动清理日志)


    @ApiModelProperty(value = "企业存储空间报警设置", name = "storeTip", required = true)
    private Integer storeTip;//企业存储空间报警设置


}