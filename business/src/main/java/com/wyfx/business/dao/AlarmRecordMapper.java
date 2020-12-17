package com.wyfx.business.dao;

import com.wyfx.business.entity.AlarmRange;
import com.wyfx.business.entity.AlarmRecord;
import com.wyfx.business.entity.vo.AlarmRecordExcelVo;
import com.wyfx.business.entity.vo.AlarmRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AlarmRecord alarmRecord);

    int insertSelective(AlarmRecord alarmRecord);

    AlarmRecord selectByPrimaryKey(Long rid);

    int updateByPrimaryKeySelective(AlarmRecord alarmRecord);

    int updateByPrimaryKeyWithBLOBs(AlarmRecord alarmRecord);

    int updateByPrimaryKey(AlarmRange alarmRange);

    List<AlarmRecordVo> selectRecord(@Param("alarmRecord") AlarmRecordVo alarmRecordVo);

    List<AlarmRecordVo> selectRecordByAid(@Param("pid") Long pid, @Param("aid") Long aid, @Param("businessId") Long businessId);

    /**
     * 导出报警记录
     * create by wsm on 2020-1-10
     */
    List<AlarmRecordExcelVo> exportExcelRecord(@Param("ids") List<Long> ids);


    /**
     * 查询未读的报警记录
     * create by wsm on 2020-1-10
     *
     * @param aid alarm_type中的aid
     * @return
     */
    List<Long> selectUnreadRecordByAid(@Param("aid") Long aid);

    /**
     * 根据类型批量更新报警记录
     *
     * @param type alarm_type 中的aid相关
     * @param ids
     * @return
     */
    int updateByIdsAndType(@Param("type") Long type, @Param("ids") List<Long> ids);

    void deleteAll();


}