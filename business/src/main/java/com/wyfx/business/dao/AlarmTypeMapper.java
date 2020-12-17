package com.wyfx.business.dao;

import com.wyfx.business.entity.AlarmType;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmTypeMapper {
    int deleteByPrimaryKey(Long aid);

    int insert(AlarmType record);

    int insertSelective(AlarmType record);

    AlarmType selectByPrimaryKey(Long aid);

    int updateByPrimaryKeySelective(AlarmType record);

    int updateByPrimaryKey(AlarmType record);
}