package com.wyfx.business.dao;

import com.wyfx.business.entity.RangeClient;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeClientMapper {
    int deleteByPrimaryKey(Long rcId);

    int insert(RangeClient record);

    int insertSelective(RangeClient record);

    RangeClient selectByPrimaryKey(Long rcId);

    int updateByPrimaryKeySelective(RangeClient record);

    int updateByPrimaryKey(RangeClient record);
}