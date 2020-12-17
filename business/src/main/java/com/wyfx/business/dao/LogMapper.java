package com.wyfx.business.dao;

import com.wyfx.business.entity.Log;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface LogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    /**
     * 通过不同条件查询日志信息
     *
     * @param accountType
     * @param actionType
     * @param startTime
     * @param endTime
     * @param executor
     * @return
     */
    List<Map> selectByCondition(@Param("businessId") Long businessId, @Param("accountType") Integer accountType, @Param("actionType") Integer actionType,
                                @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("executor") String executor);

    /**
     * 查询id集合的数据
     * todo 暂时未用到
     *
     * @param logIds
     * @return
     */
    List<Log> selectByLogIds(@Param("logIds") List<Long> logIds);

    List<Log> selectAll(@Param("businessId") Long businessId);

    /**
     * 通过bid查询该用户的所有操作日志
     *
     * @param bid
     * @return
     */
    List<Log> selectByBid(@Param("bid") Long bid);

}