package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.AlarmRecord;
import com.wyfx.business.entity.vo.AlarmRecordExcelVo;
import com.wyfx.business.entity.vo.AlarmRecordVo;

import java.util.List;
import java.util.Map;

public interface AlarmRecordService {

    void deleteRecord(String ids);

    void deleteAll();

    void updateViewStatus(Long id);

    void addRecord(AlarmRecord alarmRecord);

    PageInfo<AlarmRecordVo> selectRecord(Integer page, Integer limit, AlarmRecordVo alarmRecordVo);

    PageInfo<AlarmRecordVo> selectRecordByAid(Long pid, Long aid, Long businessId, Integer page, Integer limit);

    /**
     * 根据报警类型查询报警信息阅读状态
     *
     * @param
     * @return
     */
    Map selectRecordStatusByAid(Long type);

    /**
     * 根据类型批量更新报警记录阅读状态
     *
     * @param type aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警
     * @param ids  记录id集合
     * @return
     */
    boolean updateRecordStatusByAidsAndType(Long type, List<Long> ids);

    @Deprecated
    List<AlarmRecordExcelVo> exportExcelDate(List<Long> ids);

    /**
     * 根据条件导出所有报警记录 2020-3-12
     *
     * @param alarmRecordVo
     * @return
     */
    List<AlarmRecordVo> selectRecordToExcel(AlarmRecordVo alarmRecordVo);

}
