package com.wyfx.business.service.sys;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.MissedCalls;

import java.util.Date;
import java.util.List;

public interface MissedCallServer {
    /**
     * 添加未接来电记录
     *
     * @param userName
     * @param missedCalls
     */
    void addMissedCall(String userName, MissedCalls missedCalls);

    /**
     * 删除选中未接来电记录
     *
     * @param mid
     */
    void deleteMissedCall(Long[] mid, Long businessId);

    /**
     * 清除调度员未接来电记录
     *
     * @param bid
     */
    void clearMissedCall(Long bid, Long businessId);

    /**
     * 根据调度员以及通话类型查询记录
     *
     * @param bid
     * @param type
     * @return
     */
    PageInfo<MissedCalls> findMissedCalls(Long bid, Integer type, Integer pageSize, Integer pageNum, Long businessId);

    /**
     * 根据调度员以及呼叫时间段查询记录
     *
     * @param bid
     * @param begin
     * @return
     */
    PageInfo<MissedCalls> findMissedCallsByTime(Long bid, Date begin, Date end, Integer pageSize, Integer pageNum, Long businessId);

    /**
     * 导出选中的未接来电记录
     *
     * @param mid
     */
    List<MissedCalls> exportMissedCalls(List<Long> mid, Long businessId);

    List<Long> getNotViewCount(Long businessId);

    int updateStatusForeach(List<Integer> list);
}
