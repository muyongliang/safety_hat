package com.wyfx.business.service;

import com.wyfx.business.entity.AlarmSetting;

public interface AlarmSettingService {
    /**
     * 更新报警设置
     *
     * @param alarmId
     * @param alarmSetting
     */
    void updateSetting(Long alarmId, AlarmSetting alarmSetting);

    AlarmSetting findByProjectId(Integer projectId);
}
