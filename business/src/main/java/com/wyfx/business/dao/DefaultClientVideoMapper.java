package com.wyfx.business.dao;

import com.wyfx.business.entity.DefaultClientVideo;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultClientVideoMapper {
    int deleteByPrimaryKey(Long vid);

    int insert(DefaultClientVideo record);

    int insertSelective(DefaultClientVideo record);

    DefaultClientVideo selectByPrimaryKey(Long vid);

    DefaultClientVideo selectByBusinessId(Long businessId);

    int updateByPrimaryKeySelective(DefaultClientVideo record);

    int updateByPrimaryKey(DefaultClientVideo record);
}