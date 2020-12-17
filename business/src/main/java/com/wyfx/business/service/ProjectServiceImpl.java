package com.wyfx.business.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.AlarmSettingMapper;
import com.wyfx.business.dao.ClientAccountMapper;
import com.wyfx.business.dao.ProjectInfoMapper;
import com.wyfx.business.entity.AlarmSetting;
import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.vo.ClientAccountSelectVo;
import com.wyfx.business.entity.vo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 项目业务处理类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private AlarmSettingMapper alarmSettingMapper;
    @Autowired
    private ClientAccountMapper clientAccountMapper;

    /**
     * 添加或更新项目
     *
     * @param projectInfo
     */
    @Override
    @Transactional
    public Long updateProjectInfo(Integer projectId, ProjectInfo projectInfo) {
        if (projectId == null || projectId == 0) {
            //执行新增操作
            projectInfoMapper.insertSelective(projectInfo);
            //添加报警设置
            AlarmSetting alarmSetting = new AlarmSetting(null, 1, 1, null, 0, projectInfo.getPid(), null, null);
            alarmSettingMapper.insertSelective(alarmSetting);
        } else {
            //执行更新操作
            projectInfoMapper.updateByPrimaryKeySelective(projectInfo);
        }
        return projectInfo.getPid();
    }

    /**
     * 按状态查询项目信息
     *
     * @param businessId
     * @param status     项目状态{0:进行中;1:已结束}
     * @return
     */
    @Override
    public List findByStatus(Integer businessId, Integer status, Long bid, Long projectId) {
        return projectInfoMapper.findByStatus(businessId, status, bid, projectId);
    }

    /**
     * 按照项目名称查询
     *
     * @param businessId
     * @param projectName
     * @return
     */
    @Override
    public List<ProjectInfo> findByName(Integer businessId, String projectName) {
        return projectInfoMapper.findByName(businessId, projectName);
    }

    /**
     * 查询可移动的项目列表
     *
     * @param businessId
     * @param projectId
     * @return
     */
    @Override
    public List<ProjectInfo> findMoveList(Integer businessId, Integer projectId) {
        return projectInfoMapper.findMoveList(businessId, projectId);
    }

    /**
     * 获取项目列表
     *
     * @param businessId
     * @param bid
     * @param userType
     * @return
     */
    @Override
    public List<ProjectVo> getProjectList(Long businessId, Long bid, int userType) {
        List<ProjectVo> list = (userType == 0) ? projectInfoMapper.selectByBusinessId(businessId) : projectInfoMapper.selectByBid(bid);
        return list;
    }

    /**
     * 首页--查询项目信息
     * 2019-12-4
     *
     * @param projectName
     * @param status
     * @return
     */
    @Override
    public Map selectProjectByStatusAndPName(Long businessId, String projectName, Integer status, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> projectInfos = projectInfoMapper.selectByProjectNameAndStatus(projectName, status, businessId);
        PageInfo pageInfo = new PageInfo(projectInfos);
        Map respMap = new HashMap();
        respMap.put("project", pageInfo);
        respMap.put("count", pageInfo.getTotal());//count 本次查询的总数
//        respMap.put("count", projectInfoMapper.selectByProjectNameAndStatus(projectName, status,businessId).size());//count 本次查询的总数
        return respMap;
    }

    /**
     * 首页---查询所有项目
     * status项目状态{0:进行中;1:已结束 2全部}
     *
     * @return
     */
    @Override
    public List<Map> selectAllProjectByStatus(Integer businessId, Integer status) {

        List<ProjectInfo> allProject;
        if (status == 2) {
            allProject = projectInfoMapper.findByBusinessIdAndStatus(businessId, null);
        } else {
            allProject = projectInfoMapper.findByBusinessIdAndStatus(businessId, status);
        }
        List respList = new ArrayList();
        for (ProjectInfo projectInfo : allProject) {
            Map map = new HashMap();
            map.put("projectName", projectInfo.getProjectName());
            map.put("pid", projectInfo.getPid());
            map.put("address", projectInfo.getProjectAddress());
            map.put("longitude", projectInfo.getLongitude());
            map.put("latitude", projectInfo.getLatitude());
            respList.add(map);
        }
        return respList;
    }

    /**
     * 首页----查询项目统计
     * 2019-12-4
     *
     * @param businessId
     * @return
     */
    @Override
    public List<Map> selectProjectStatistics(Integer businessId) {
        //查询所以项目数
        List<ProjectInfo> allProjectList = projectInfoMapper.findByBusinessId(businessId);
        Integer allProject = allProjectList.size();
        //项目状态{0:进行中;1:已结束} 进行中的项目数
        List<ProjectInfo> onBuildProjectList = projectInfoMapper.findByStatus(businessId, 0, null, null);
        Integer onBuildProject = onBuildProjectList.size();
        //已完成项目数
        Integer completeProject = allProject - onBuildProject;
        List respList = new ArrayList();
        Map onBuildMap = new HashMap();
        onBuildMap.put("name", "进行中");
        onBuildMap.put("value", onBuildProject);
        respList.add(onBuildMap);
        Map allMap = new HashMap();
        allMap.put("name", "全部");
        allMap.put("value", allProject);
        respList.add(allMap);
        Map completeMap = new HashMap();
        completeMap.put("name", "已结束");
        completeMap.put("value", completeProject);
        respList.add(completeMap);
        return respList;
    }

    /**
     * 首页----查询某项目的终端统计
     * -----------------查询项目的终端统计
     *
     * @param projectId
     * @return
     */
    @Override
    public List<Map> selectClientStatistics(Integer projectId) {
        //离线终端
        Integer disOnlineClient = clientAccountMapper.selectClientByOnLineStatus(0, projectId).size();
        //所有终端
        List<ClientAccountSelectVo> allAccount = clientAccountMapper.findAllAccount(projectId);
        System.out.println(allAccount);
        Integer allClient = allAccount.size();
        List respList = new ArrayList();
        Map onBuildMap = new HashMap();
        onBuildMap.put("name", "在线数");
        onBuildMap.put("value", allClient - disOnlineClient);
        respList.add(onBuildMap);
        Map allMap = new HashMap();
        allMap.put("name", "全部");
        allMap.put("value", allClient);
        respList.add(allMap);
        Map completeMap = new HashMap();
        completeMap.put("name", "离线数");
        completeMap.put("value", disOnlineClient);
        respList.add(completeMap);
        return respList;
    }

    @Override
    public ProjectInfo findByProjectId(Long projectId) {
        return projectInfoMapper.selectByPrimaryKey(projectId);
    }
}
