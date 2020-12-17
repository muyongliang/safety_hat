package com.wyfx.business.dao;

import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.vo.ProjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectInfoMapper {
    int deleteByPrimaryKey(String pid);

    int insert(ProjectInfo record);

    int insertSelective(ProjectInfo record);

    ProjectInfo selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(ProjectInfo record);

    int updateByPrimaryKey(ProjectInfo record);

    List<ProjectInfo> findByBusinessId(@Param("businessId") Integer businessId);

    List<ProjectInfo> findByStatus(@Param("businessId") Integer businessId, @Param("status") Integer status, @Param("bid") Long bid, @Param("projectId") Long projectId);

    List<ProjectInfo> findByName(@Param("businessId") Integer businessId, @Param("projectName") String projectName);


    /**
     * 查询可移至的项目列表
     *
     * @param businessId
     * @param projectId
     * @return
     */
    List<ProjectInfo> findMoveList(@Param("businessId") Integer businessId, @Param("pid") Integer projectId);

    List<ProjectVo> selectByBid(@Param("bid") Long bid);

    List<ProjectVo> selectByBusinessId(@Param("businessId") Long businessId);

    /**
     * 首页---通过项目名和状态查询项目信息
     *
     * @param status
     * @param projectName
     * @return
     */
    List<Map> selectByProjectNameAndStatus(@Param("projectName") String projectName, @Param("status") Integer status, @Param("businessId") Long businessId);

    /**
     * 总后台----首页  查询项目在建情况
     *
     * @param token
     * @param projectStatus
     * @return
     */
    Integer selectProjectCount(@Param("token") String token, @Param("projectStatus") Integer projectStatus);

    /**
     * 总后台 --企业明细 通过地址检索 项目状况
     *
     * @param token
     * @param projectStatus
     * @param address
     * @return 返回项目的id集合
     */
    List<Integer> selectProjectCountByAddressAndStatus(@Param("token") String token, @Param("projectStatus") Integer projectStatus, @Param("address") String address);


    /**
     * 通过企业id和项目完成状况查询
     *
     * @param businessId
     * @param status
     * @return
     */
    List<ProjectInfo> findByBusinessIdAndStatus(@Param("businessId") Integer businessId, @Param("status") Integer status);

}