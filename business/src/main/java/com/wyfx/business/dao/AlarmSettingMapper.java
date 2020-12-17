package com.wyfx.business.dao;

import com.wyfx.business.entity.AlarmSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmSettingMapper {
    int deleteByPrimaryKey(Long alarmId);

    int insert(AlarmSetting record);

    int insertSelective(AlarmSetting record);

    AlarmSetting selectByPrimaryKey(Long alarmId);

    int updateByPrimaryKeySelective(AlarmSetting record);

    int updateByPrimaryKey(AlarmSetting record);

    AlarmSetting findByProjectId(@Param("projectId") Long projectId);
}