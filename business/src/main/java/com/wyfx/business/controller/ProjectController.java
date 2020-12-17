package com.wyfx.business.controller;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.vo.ProjectVo;
import com.wyfx.business.service.ProjectService;
import com.wyfx.business.service.TalkBackService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.AccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 项目管理
 */
@RestController
@RequestMapping("/project")
@Api(value = "ProjectController", tags = {"项目控制器"})
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TalkBackService talkBackService;
    @Autowired
    private BusinessUserService businessUserService;

    /**
     * 添加项目信息,并创建默认对讲组
     *
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    @RequiresPermissions("project:add")
    @AopLog(describe = "创建项目：", targetParamName = "projectInfo", operationType = OperationType.INSERT)
    public Object addProject(@RequestBody ProjectInfo projectInfo) {
        BusinessUser user = getCurrentUser();
        projectInfo.setBusinessId(user.getBusinessId());

        if (!AccountUtil.vilicationPhone(projectInfo.getPicPhone())) {
            return new MyResponseEntity(500, "电话号码格式错误");
        }
        Long projectId = projectService.updateProjectInfo(null, projectInfo);
        TalkBackGroup talkBackGroup = new TalkBackGroup(null, "默认分组", 1, 0, 0, new Date(), user.getUserName(), user.getBusinessId(), projectId);
        talkBackService.updateTalkBack(null, talkBackGroup, user, false);

        //添加对讲组:默认分组成员(调度员)
        /*talkBackService.updateMember(talkBackGroup.getId(),user.getBusinessId(),new Object[0],projectId);*/

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/checkIsAllowEndProject", method = RequestMethod.GET)
    @ApiOperation(value = "检查是否可以结束选中项目")
    @ApiImplicitParam(value = "项目Id", name = "projectId")
    public Object checkIsAllowEndProject(Long projectId) {
        boolean flag = true;
        if (projectId != null) {
            PageInfo<BusinessUser> list = businessUserService.findByProjectId(projectId.intValue(), null, null);
            if (list != null && list.getList().size() > 0) {
                flag = false;
            }
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), flag);
    }

    /**
     * 获取项目列表:
     * 调度员仅有自己所在项目的数据权限,管理员可查看所有的项目列表
     *
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    @RequiresPermissions("project:view")
    public Object getProjectList() {
        BusinessUser businessUser = getCurrentUser();
        List<ProjectVo> list = projectService.getProjectList(businessUser.getBusinessId(), businessUser.getBid(), businessUser.getUserType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 更新项目信息
     *
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    @RequiresPermissions("project:edit")
    @AopLog(describe = "编辑项目：", targetParamName = "projectInfo", operationType = OperationType.UPDATE)
    public Object updateProject(@RequestBody ProjectInfo projectInfo) {
        if (projectInfo.getPid() != null) {
            PageInfo<BusinessUser> list = businessUserService.findByProjectId(projectInfo.getPid().intValue(), null, null);
            if (list != null && list.getList().size() > 0 && projectInfo.getStatus() == 1) {
                return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "不能结束当前项目");
            }
        }
        projectService.updateProjectInfo(projectInfo.getPid().intValue(), projectInfo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 根据状态查询项目
     *
     * @param status
     * @return
     */
    @RequestMapping(value = "/findByStatus", method = RequestMethod.GET)
    public Object findByStatus(Integer status) {
        BusinessUser user = getCurrentUser();
        if (user == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "请重新登录");
        }
        Long bid = (user.getUserType() == 0 || user.getUserType() == 3) ? null : user.getBid();
        List<ProjectInfo> list = projectService.findByStatus(user.getBusinessId().intValue(), status, bid, user.getProjectId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 根据项目名称模糊查询
     *
     * @param projectName
     * @return
     */
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public Object findByName(String projectName) {
        List<ProjectInfo> list = projectService.findByName(getCurrentUser().getBusinessId().intValue(), projectName);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


    /**
     * 首页------------企业项目统计查询 通过企业id查询企业项目状况
     *
     * @return
     */
    @GetMapping("/selectBusinessStatistics")
    public Object selectBusinessStatistics() {
        try {
            Integer businessId = getCurrentUser().getBusinessId().intValue();
            List<Map> mapList = projectService.selectProjectStatistics(businessId);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), mapList);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未登录");
        }

    }


    /**
     * 首页----通过名字和状态查询企业信息
     *
     * @param projectName
     * @param status
     * @return
     */
    @GetMapping("/handleSelectProject")
    public Object handleSelectProject(String projectName, Integer status, Integer pageNum, Integer pageSize) {
        System.out.println("projectName=" + projectName + "status=====" + status);
        try {
            Map map = projectService.selectProjectByStatusAndPName(getCurrentUser().getBusinessId(), projectName, status, pageNum, pageSize);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未登录");
        }
    }


    /**
     * 首页---通过选取的项目id 查询项目的终端统计
     *
     * @param projectId
     * @return
     */
    @GetMapping("/handleSelectClientStatistics")
    public Object handleSelectClientStatistics(Integer projectId) {
        List<Map> mapList = projectService.selectClientStatistics(projectId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), mapList);
    }

    /**
     * 首页 -----查询所有项目经纬度及名字
     * 通过完成情况查询 status项目状态{0:进行中;1:已结束 2全部}
     *
     * @return
     */
    @GetMapping("/handleSelectAllProject")
    public Object handleSelectAllProject(Integer status) {
        try {
            List allProject = projectService.selectAllProjectByStatus(getCurrentUser().getBusinessId().intValue(), status);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), allProject);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未登录");
        }

    }


}
