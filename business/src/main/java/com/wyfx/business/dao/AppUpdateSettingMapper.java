package com.wyfx.business.dao;

import com.wyfx.business.entity.AppUpdateSetting;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUpdateSettingMapper {
    int deleteByPrimaryKey(Long appId);

    int insert(AppUpdateSetting record);

    int insertSelective(AppUpdateSetting record);

    AppUpdateSetting selectByPrimaryKey(Long appId);

    int updateByPrimaryKeySelective(AppUpdateSetting record);

    int updateByPrimaryKey(AppUpdateSetting record);


}