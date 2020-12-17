package com.wyfx.business.dao;

import com.wyfx.business.entity.BaiduAlarm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BaiduAlarmMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaiduAlarm record);

    int insertSelective(BaiduAlarm record);

    BaiduAlarm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaiduAlarm record);

    int updateByPrimaryKey(BaiduAlarm record);

    List<Map> selectOnlineClientByFenceId(@Param("fenceId") Long fenceId);
}