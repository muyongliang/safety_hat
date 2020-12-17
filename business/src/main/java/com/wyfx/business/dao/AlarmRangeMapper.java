package com.wyfx.business.dao;

import com.wyfx.business.entity.AlarmRange;
import com.wyfx.business.entity.vo.ProjectVo;
import com.wyfx.business.entity.vo.RangeMemberVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRangeMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(AlarmRange record);

    int insertSelective(AlarmRange record);

    AlarmRange selectByPrimaryKey(Long rid);

    int updateByPrimaryKeySelective(AlarmRange record);

    int updateByPrimaryKeyWithBLOBs(AlarmRange record);

    int updateByPrimaryKey(AlarmRange record);

    List<ProjectVo> selectByBid(@Param("bid") Long bid);

    List<ProjectVo> selectByBusinessId(@Param("businessId") Long businessId);

    /**
     * 根据百度api的围栏ID删除记录
     *
     * @param fenceId
     * @return
     */
    int deleteByFenceId(@Param("fenceId") Integer fenceId);

    /**
     * 查询项目下所有的电子围栏信息
     *
     * @param projectId
     * @return
     */
    List<RangeMemberVo> selectByProjectId(@Param("projectId") Integer projectId);

    List<RangeMemberVo> selectByProjectIdAndTypeAndName(@Param("businessId") Long businessId, @Param("projectId") Long projectId, @Param("name") String name, @Param("type") Integer type);

    List<RangeMemberVo> selectByName(@Param("projectId") Integer projectId, @Param("name") String name);

    List<RangeMemberVo> selectByType(@Param("projectId") Integer projectId, @Param("type") Integer type);

    /**
     * 通过电子围栏成员Bid查询成员所在围栏信息
     *
     * @param status 是否在围栏
     * @param bid    成员ID
     * @return
     */
    List<AlarmRange> selectRangesByBid(@Param("status") int status, @Param("bid") Long bid);

}