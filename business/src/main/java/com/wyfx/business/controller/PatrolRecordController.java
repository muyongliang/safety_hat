package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.constant.RecordTypeEnum;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.PatrolRecordDetailVo;
import com.wyfx.business.entity.vo.PatrolRecordExcelVo;
import com.wyfx.business.entity.vo.RecordManagerVo;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author HQH
 * @date 2019/12/9
 * @description 巡检报告
 */
@RestController
@RequestMapping("/patrolRecord")
public class PatrolRecordController extends BaseController {

    @Autowired
    private RecordManagerService recordManagerService;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private IExcelService iExcelService;

    /**
     * 添加巡检报告记录
     *
     * @param recordManager （pid sendBid title） event  content  result  imgUrls
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Object addRecord(String userName, RecordManager recordManager,
                            @RequestParam(required = false, value = "event") String event,
                            @RequestParam(required = false, value = "content") String content,
                            @RequestParam(required = false, value = "result") String result,
                            @RequestParam(required = false, value = "imgUrls") String imgUrls) {
        BusinessUser businessUser = businessUserService.findByUserName(userName);
        recordManager.setSendBid(businessUser.getBid());
        recordManager.setPid(businessUser.getProjectId());
        recordManager.setSendName(businessUser.getName());
        recordManager.setType(RecordTypeEnum.patrol.getType());
        PatrolRecordDetailVo patrolRecordDetailVo = new PatrolRecordDetailVo();
        patrolRecordDetailVo.setEvent(event);
        patrolRecordDetailVo.setContent(content);
        patrolRecordDetailVo.setResult(result);
        patrolRecordDetailVo.setImgUrls(imgUrls);
        patrolRecordDetailVo.setTitle(recordManager.getTitle());


        String message = JSONObject.toJSONString(patrolRecordDetailVo);
        recordManager.setMessage(message);
        recordManagerService.addRecord(recordManager);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询巡检报告记录明细
     *
     * @param id 巡检报告id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions("patrolRecord:viewDetail")
    public Object detail(@PathVariable("id") Long id) {
        RecordManager recordManager = recordManagerService.selectDetail(id);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), recordManager);
    }

    @RequestMapping(value = "/detailByApp/{id}", method = RequestMethod.GET)
    public Object detailByApp(@PathVariable("id") Long id) {
        Object responseEntity = detail(id);
        return responseEntity;
    }

    /**
     * 删除指定记录
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("patrolRecord:delete")
    public Object checkedRecord(String ids) {
        recordManagerService.deleteRecord(ids);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 清空记录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = RequestMethod.POST)
    @RequiresPermissions("patrolRecord:clear")
    @AopLog(describe = "清空巡检记录", operationType = OperationType.CLEAR)
    public Object clearRecord() {
        recordManagerService.deleteAll(RecordTypeEnum.patrol.getType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 巡检报告记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("patrolRecord:view")
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "projectId") Long projectId,
                       @RequestParam(required = false, value = "startTime") String startTime,
                       @RequestParam(required = false, value = "endTime") String endTime,
                       @RequestParam(required = false, value = "name") String name,
                       @RequestParam(required = false, value = "userName") String userName
    ) {
        BusinessUser businessUser = getCurrentUser();
        /*Integer userType = businessUser.getUserType();
        if(0 != userType){
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(),"该功能只提供给企业管理员！");
        }*/
        projectId = (projectId == null && businessUser.getUserType() == 1) ? businessUser.getProjectId() : projectId;
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        recordManagerVo.setSendName(name);
        recordManagerVo.setPid(projectId);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = null;
            if ((user = businessUserService.findByUserName(userName)) == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
        }

        try {
            recordManagerVo.setStartTime(DateUtil.completionDate(startTime, true));
            recordManagerVo.setEndTime(DateUtil.completionDate(endTime, false));
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }
        recordManagerVo.setType(RecordTypeEnum.patrol.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        PageInfo<RecordManagerVo> list = recordManagerService.selectRecord(page, limit, recordManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    @RequestMapping(value = "/listByApp", method = RequestMethod.GET)
    public Object listByApp(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                            @RequestParam(required = false, value = "projectId") Long projectId,
                            @RequestParam(required = false, value = "startTime") String startTime,
                            @RequestParam(required = false, value = "endTime") String endTime,
                            @RequestParam(required = false, value = "name") String name,
                            @RequestParam(required = false, value = "userName") String userName) {
        Object responseEntity = list(page, limit, projectId, startTime, endTime, name, userName);
        return responseEntity;
    }


    /**
     * 按条件导出巡检报告
     * 2020-3-12
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @param name
     * @param userName
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportPatrolRecord", method = RequestMethod.POST)
    @RequiresPermissions("patrolRecord:export")
    public Object exportPatrolRecord(
            @RequestParam(required = false, value = "projectId") Long projectId,
            @RequestParam(required = false, value = "startTime") String startTime,
            @RequestParam(required = false, value = "endTime") String endTime,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "userName") String userName,
            HttpServletResponse response
    ) {
        BusinessUser businessUser = getCurrentUser();
        /*Integer userType = businessUser.getUserType();
        if(0 != userType){
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(),"该功能只提供给企业管理员！");
        }*/
        projectId = (projectId == null && businessUser.getUserType() == 1) ? businessUser.getProjectId() : projectId;
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        recordManagerVo.setSendName(name);
        recordManagerVo.setPid(projectId);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = null;
            if ((user = businessUserService.findByUserName(userName)) == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
        }

        try {
            recordManagerVo.setStartTime(DateUtil.completionDate(startTime, true));
            recordManagerVo.setEndTime(DateUtil.completionDate(endTime, false));
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }
        recordManagerVo.setType(RecordTypeEnum.patrol.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());

        List<PatrolRecordExcelVo> listC = recordManagerService.exportPatrolRecord(recordManagerVo);
        iExcelService.exportExcel(listC, PatrolRecordExcelVo.class, RecordTypeEnum.patrol.getName(), response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 导出巡检excel
     *
     * @return
     */
    @RequestMapping(value = "/exportPatrolRecordExcel", method = RequestMethod.POST)
    @RequiresPermissions("patrolRecord:export")
    @Deprecated
    public Object exportPatrolRecordExcel(HttpServletResponse response, String ids) {
        List<Long> list;
        if (ids == null || "".equals(ids)) {
            list = null;
        } else {
            list = JSONArray.parseArray(ids).toJavaList(Long.class);
        }
        List<PatrolRecordExcelVo> listC = recordManagerService.exportPatrolRecord(list);
        iExcelService.exportExcel(listC, PatrolRecordExcelVo.class, RecordTypeEnum.patrol.getName(), response);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }
}
