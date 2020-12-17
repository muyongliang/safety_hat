package com.wyfx.business.dao;

import com.wyfx.business.entity.AlarmTrackPoint;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmTrackPointMapper {
    int deleteByPrimaryKey(Long tid);

    int insert(AlarmTrackPoint record);

    int insertSelective(AlarmTrackPoint record);

    AlarmTrackPoint selectByPrimaryKey(Long tid);

    int updateByPrimaryKeySelective(AlarmTrackPoint record);

    int updateByPrimaryKey(AlarmTrackPoint record);
}