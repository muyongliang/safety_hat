package com.wyfx.total.mapper;

import com.wyfx.total.entity.BusinessOtherSetting;

public interface BusinessOtherSettingMapper {
    int deleteByPrimaryKey(Long otherId);

    int insert(BusinessOtherSetting record);

    int insertSelective(BusinessOtherSetting record);

    BusinessOtherSetting selectByPrimaryKey(Long otherId);

    int updateByPrimaryKeySelective(BusinessOtherSetting record);

    int updateByPrimaryKey(BusinessOtherSetting record);
}