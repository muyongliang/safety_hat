package com.wyfx.total.mapper;

import com.wyfx.total.entity.DefaultSetting;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultSettingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DefaultSetting record);

    int insertSelective(DefaultSetting record);

    DefaultSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DefaultSetting record);

    int updateByPrimaryKey(DefaultSetting record);

}