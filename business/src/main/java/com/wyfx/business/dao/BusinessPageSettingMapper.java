package com.wyfx.business.dao;

import com.wyfx.business.entity.BusinessPageSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessPageSettingMapper {
    int deleteByPrimaryKey(String pid);

    int insert(BusinessPageSetting record);

    int insertSelective(BusinessPageSetting record);

    BusinessPageSetting selectByPrimaryKey(String pid);

    int updateByPrimaryKeySelective(BusinessPageSetting record);

    int updateByPrimaryKey(BusinessPageSetting record);

    BusinessPageSetting selectByBusinessId(@Param("businessId") Long businessId);
}