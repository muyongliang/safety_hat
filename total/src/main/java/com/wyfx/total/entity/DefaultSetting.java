package com.wyfx.total.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSetting {
    private Long id;

    private Integer vedioTimeLimit; //无限制提交0 有限制提交限制值

    private Integer isAutoUpload;

    private Integer dispatcherUploadLimit;

    private Integer appLog;//0 不自动清理日志 其他为清理日志的天数

    private Integer dispatcherLog;

    private Integer tipTime;

    private Integer thirdTipTime;

    private Integer storeTip;
}