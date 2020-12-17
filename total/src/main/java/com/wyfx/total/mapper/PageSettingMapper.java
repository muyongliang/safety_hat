package com.wyfx.total.mapper;

import com.wyfx.total.entity.PageSetting;
import org.springframework.stereotype.Repository;

@Repository
public interface PageSettingMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(PageSetting record);

    int insertSelective(PageSetting record);

    PageSetting selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(PageSetting record);

    int updateByPrimaryKey(PageSetting record);
}