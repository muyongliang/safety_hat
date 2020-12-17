package com.wyfx.business.dao;

import com.wyfx.business.entity.RangeAlarmRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RangeAlarmRecordMapper {
    int deleteByPrimaryKey(Long recordId);

    int insert(RangeAlarmRecord record);

    int insertSelective(RangeAlarmRecord record);

    RangeAlarmRecord selectByPrimaryKey(Long recordId);

    int updateByPrimaryKeySelective(RangeAlarmRecord record);

    int updateByPrimaryKey(RangeAlarmRecord record);

    RangeAlarmRecord selectByDateAndRid(@Param("rid") Long rid, @Param("date") String date);

    RangeAlarmRecord selectByDateAndFenceId(@Param("fenceId") Integer fenceId, @Param("date") String date);

    List<Map> selectOnlineClientByRid(@Param("rid") Long rid);

    void deleteAll();
}