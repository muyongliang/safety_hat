package com.wyfx.business.dao;

import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordManagerMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RecordManager recordManager);

    int insertSelective(RecordManager recordManager);

    RecordManager selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RecordManager recordManager);

    int updateByPrimaryKeyWithBLOBs(RecordManager recordManager);

    int updateByPrimaryKey(RecordManager recordManager);

    List<RecordManagerVo> selectRecord(@Param("RecordManager") RecordManagerVo recordManagerVo);

    List<RecordManagerVo> selectPatrolRecord(@Param("RecordManager") RecordManagerVo recordManagerVo);//查询巡检报告

    List<RecordManagerVo> selectPhoneRecord(@Param("RecordManager") RecordManagerVo recordManagerVo);

    List<TalkBackRecordVo> selectTalkBackRecord(@Param("TalkBackRecordVo") TalkBackRecordVo talkBackRecordVo);

    /**
     * 按条件导出广播记录
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    List<BroadcastRecordExcelVo> exportBroadcastDate(@Param("RecordManager") RecordManagerVo recordManagerVo);

    /**
     * 按条件导出电话记录
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    List<PhoneRecordExcelVo> exportPhoneRecordDate(@Param("RecordManager") RecordManagerVo recordManagerVo);

    /**
     * 按条件导出巡检报告
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    List<PatrolRecordExcelVo> exportPatrolRecordDate(@Param("RecordManager") RecordManagerVo recordManagerVo);

    /**
     * 按条件导出对讲记录
     * 2020-3-12
     *
     * @param talkBackRecordVo
     * @return
     */
    List<TalkBackRecordExcelVo> exportTalkBackRecordDate(@Param("RecordManager") RecordManagerVo talkBackRecordVo);

    void deleteAll(@Param("type") Integer type);

    void deleteTalkBackRecords(@Param("groupId") Long groupId, @Param("pid") Long pid);

    /**
     * 导出广播记录
     *
     * @param ids
     * @return
     */
    @Deprecated
    List<BroadcastRecordExcelVo> exportBroadcastRecord(@Param("ids") List<Long> ids);

    /**
     * 导出电话录音记录
     *
     * @param ids
     * @return
     */
    @Deprecated
    List<PhoneRecordExcelVo> exportPhoneRecord(@Param("ids") List<Long> ids);

    /**
     * 导出巡检记录
     *
     * @param ids
     * @return
     */
    @Deprecated
    List<PatrolRecordExcelVo> exportPatrolRecord(@Param("ids") List<Long> ids);

    /**
     * 导出对讲记录明细
     *
     * @param ids
     * @return
     */
    @Deprecated
    List<TalkBackRecordExcelVo> exportTalkBackRecord(@Param("ids") List<Long> ids);


}