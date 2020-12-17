package com.wyfx.business.service.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.MissedCallsMapper;
import com.wyfx.business.entity.MissedCalls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/12/11
 * @description 未接来电记录
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MissedCallServerImpl implements MissedCallServer {

    @Autowired
    private MissedCallsMapper missedCallsMapper;


    /**
     * 添加未接来电记录
     *
     * @param userName
     * @param missedCalls
     */
    @Override
    @Transactional
    public void addMissedCall(String userName, MissedCalls missedCalls) {
        missedCallsMapper.insertSelective(missedCalls);
    }

    /**
     * 删除选中未接来电记录
     *
     * @param mid
     */
    @Override
    @Transactional
    public void deleteMissedCall(Long[] mid, Long businessId) {
        missedCallsMapper.deleteForeach(mid, businessId);
    }

    /**
     * 清除调度员未接来电记录
     *
     * @param bid
     */
    @Override
    @Transactional
    public void clearMissedCall(Long bid, Long businessId) {
        missedCallsMapper.clearMissedCall(bid, businessId);
    }

    /**
     * 根据调度员以及通话类型查询记录
     *
     * @param bid
     * @param type
     * @return
     */
    @Override
    public PageInfo<MissedCalls> findMissedCalls(Long bid, Integer type, Integer pageSize, Integer pageNum, Long businessId) {
        PageHelper.startPage(pageNum, pageSize);
        List<MissedCalls> list = (type == null || type == 0) ? missedCallsMapper.selectByBid(bid, businessId) : missedCallsMapper.selectByBidAndType(bid, type, businessId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据调度员以及呼叫时间段查询记录
     *
     * @param bid
     * @param begin
     * @param end
     * @return
     */
    @Override
    public PageInfo<MissedCalls> findMissedCallsByTime(Long bid, Date begin, Date end, Integer pageSize, Integer pageNum, Long businessId) {
        PageHelper.startPage(pageNum, pageSize);
        List<MissedCalls> list = missedCallsMapper.selectByBidAndTime(bid, begin, end, businessId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 导出选中的未接来电记录
     *
     * @param mid
     */
    @Override
    public List<MissedCalls> exportMissedCalls(List<Long> mid, Long businessId) {
        List<MissedCalls> missedCalls = missedCallsMapper.selectByIds(mid, businessId);
        return missedCalls;
    }

    @Override
    public List<Long> getNotViewCount(Long businessId) {
        return missedCallsMapper.selectNotViewCount(businessId);
    }

    @Override
    @Transactional
    public int updateStatusForeach(List<Integer> list) {
        return missedCallsMapper.updateStatusForeach(list);
    }
}
