package com.wyfx.business.service;

import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.vo.ProjectVo;

import java.util.List;
import java.util.Map;

public interface ProjectService {

    /**
     * 添加或更新项目
     *
     * @param projectInfo
     */
    Long updateProjectInfo(Integer projectId, ProjectInfo projectInfo);

    /**
     * 按状态查询项目信息
     *
     * @param businessId
     * @return
     */
    List findByStatus(Integer businessId, Integer status, Long bid, Long projectId);

    /**
     * 按照项目名称查询
     *
     * @param businessId
     * @param projectName
     * @return
     */
    List<ProjectInfo> findByName(Integer businessId, String projectName);

    /**
     * 查询可移动的项目列表
     *
     * @param businessId
     * @param projectId
     * @return
     */
    List<ProjectInfo> findMoveList(Integer businessId, Integer projectId);

    /**
     * 获取项目列表
     *
     * @param businessId
     * @param bid
     * @param userType
     * @return
     */
    List<ProjectVo> getProjectList(Long businessId, Long bid, int userType);

    /**
     * 首页----通过项目名和状态查询项目信息
     *
     * @param projectName
     * @param status
     * @return
     */
    Map selectProjectByStatusAndPName(Long businessId, String projectName, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 首页 ---查询所有项目
     * status项目状态{0:进行中;1:已结束 2全部}
     *
     * @return
     */
    List<Map> selectAllProjectByStatus(Integer businessId, Integer status);

    /**
     * 首页 ----查询项目统计
     *
     * @param businessId
     * @return
     */
    List<Map> selectProjectStatistics(Integer businessId);

    /**
     * 首页---查询某一项目的终端统计
     *
     * @param projectId
     * @return
     */
    List<Map> selectClientStatistics(Integer projectId);

    ProjectInfo findByProjectId(Long projectId);

}
