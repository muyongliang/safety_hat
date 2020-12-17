package com.wyfx.business.dao;

import com.wyfx.business.entity.MissedCalls;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MissedCallsMapper {
    int deleteByPrimaryKey(Long missId);

    int insert(MissedCalls record);

    int insertSelective(MissedCalls record);

    MissedCalls selectByPrimaryKey(Long missId);

    int updateByPrimaryKeySelective(MissedCalls record);

    int updateByPrimaryKey(MissedCalls record);

    /**
     * 批量删除选中记录
     *
     * @param missIds
     * @return
     */
    int deleteForeach(@Param("missIds") Long[] missIds, @Param("businessId") Long businessId);

    int updateStatusForeach(@Param("list") List<Integer> list);

    int clearMissedCall(@Param("bid") Long bid, @Param("businessId") Long businessId);

    List<MissedCalls> selectByBid(@Param("bid") Long bid, @Param("businessId") Long businessId);

    List<MissedCalls> selectByBidAndType(@Param("bid") Long bid, @Param("type") Integer type, @Param("businessId") Long businessId);

    List<MissedCalls> selectByBidAndTime(@Param("bid") Long bid, @Param("begin") Date begin, @Param("end") Date end, @Param("businessId") Long businessId);

    /**
     * 查询多条数据
     *
     * @param missIds
     * @return
     */
    List<MissedCalls> selectByIds(@Param("missIds") List<Long> missIds, @Param("businessId") Long businessId);

    /**
     * 统计未读消息数量
     *
     * @return
     */
    List<Long> selectNotViewCount(@Param("businessId") Long businessId);

}