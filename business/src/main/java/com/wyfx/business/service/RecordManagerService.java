package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RecordManagerService {

    void deleteRecord(String ids);

    void deleteAll(Integer type);

    void deleteByGroupIdAndPid(Long groupId, Long pid);

    void addRecord(RecordManager alarmRecord);

    PageInfo<RecordManagerVo> selectRecord(Integer page, Integer limit, RecordManagerVo recordManagerVo);

    PageInfo<RecordManagerVo> selectPhoneRecord(Integer page, Integer limit, RecordManagerVo recordManagerVo);

    PageInfo<TalkBackRecordVo> selectTalkBackRecord(Integer page, Integer limit, TalkBackRecordVo talkBackRecordVo);

    /**
     * 按条件导出广播记录
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    List<BroadcastRecordExcelVo> exportBroadcastDate(RecordManagerVo recordManagerVo);

    @Deprecated
    List<BroadcastRecordExcelVo> exportBroadcastRecord(List<Long> ids);

    /**
     * 按条件导出电话记录
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    List<PhoneRecordExcelVo> exportPhoneRecordDate(RecordManagerVo recordManagerVo);

    @Deprecated
    List<PhoneRecordExcelVo> exportPhoneRecord(List<Long> ids);

    /**
     * 按条件导出巡检报告
     * 2020-3-12
     *
     * @param alarmRecordVo
     * @return
     */
    List<PatrolRecordExcelVo> exportPatrolRecord(RecordManagerVo alarmRecordVo);

    @Deprecated
    List<PatrolRecordExcelVo> exportPatrolRecord(List<Long> ids);

    /**
     * 按条件导出对讲记录
     * 2020-3-12
     *
     * @param alarmRecordVo
     * @return
     */
    List<TalkBackRecordExcelVo> exportTalkBackRecord(RecordManagerVo alarmRecordVo);

    @Deprecated
    List<TalkBackRecordExcelVo> exportTalkBackRecord(List<Long> ids);

    RecordManager selectDetail(Long id);

    Map<String, String> getUrlById(Long id, HttpServletRequest request) throws Exception;

}
