package com.wyfx.total.mapper;

import com.wyfx.total.entity.LogInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LogInfoMapper {
    int deleteByPrimaryKey(Integer lid);

    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    LogInfo selectByPrimaryKey(Integer lid);

    int updateByPrimaryKeySelective(LogInfo record);

    int updateByPrimaryKey(LogInfo record);


    List<LogInfo> selectAll();

    List<LogInfo> selectLogInfByCondition(Integer taskType, Date startTime, Date endTime);
}