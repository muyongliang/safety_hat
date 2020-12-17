package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.constant.RecordTypeEnum;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.RecordManagerVo;
import com.wyfx.business.entity.vo.TalkBackRecordExcelVo;
import com.wyfx.business.entity.vo.TalkBackRecordVo;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author HQH
 * @date 2019/12/9
 * @description 对讲记录
 */
@RestController
@RequestMapping("/talkBackRecord")
public class TalkBackRecordController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(TalkBackRecordController.class);
    @Autowired
    private RecordManagerService recordManagerService;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IExcelService iExcelService;

    /**
     * 添加对讲记录
     *
     * @param recordManager 账号类型 messageType 1：终端    2：调度员
     *                      发送人id sendBid
     *                      内容 message
     *                      项目id  pid
     *                      对讲组id groupId
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Object addRecord(RecordManager recordManager) {
        BusinessUser businessUser = businessUserService.findByBid(recordManager.getSendBid());

        recordManager.setSendName(businessUser.getName());
        recordManager.setType(RecordTypeEnum.talkBackDetail.getType());
        recordManagerService.addRecord(recordManager);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 根据项目id和对讲组id 查询对讲记录
     *
     * @param id 电话录音id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Object detail(@PathVariable("id") Long id) {
        RecordManager recordManager = recordManagerService.selectDetail(id);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), recordManager);
    }

    /**
     * 根据记录id删除记录 多个记录用，隔开
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("talkBackRecord:delete")
    public Object delete(String ids) {
        recordManagerService.deleteRecord(ids);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 根据项目id和对讲组id删除对应的对讲记录(要删除多个请多次调用)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteByGroupIdAndPid", method = RequestMethod.POST)
    @RequiresPermissions("talkBackRecord:clear")
    public Object deleteByGroupIdAndPid(@RequestParam(required = false, value = "groupId") Long groupId,
                                        @RequestParam(required = false, value = "pid") Long pid) {
        if (null == groupId || null == pid) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "请传入正确的groupId和pid");
        }
        recordManagerService.deleteByGroupIdAndPid(groupId, pid);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 对讲记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("talkBackRecord:view")
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "groupId") Long groupId,
                       @RequestParam(required = false, value = "pid") Long pid
    ) {
        BusinessUser businessUser = getCurrentUser();
        TalkBackRecordVo talkBackRecordVo = new TalkBackRecordVo();
        talkBackRecordVo.setGroupId(groupId);

        Long projectId = (businessUser.getUserType() == 1) ? businessUser.getProjectId() : pid;
        talkBackRecordVo.setPid(projectId);

        talkBackRecordVo.setBusinessId(businessUser.getBusinessId());
        PageInfo<TalkBackRecordVo> list = recordManagerService.selectTalkBackRecord(page, limit, talkBackRecordVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


    /**
     * 对讲记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listDetail", method = RequestMethod.POST)
    @RequiresPermissions("talkBackRecord:viewDetail")
    public Object listDetail(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                             @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                             @RequestParam(required = false, value = "startTime") String startTime,
                             @RequestParam(required = false, value = "endTime") String endTime,
                             @RequestParam(required = false, value = "name") String name,
                             @RequestParam(required = false, defaultValue = "1", value = "groupId") Long groupId,
                             @RequestParam(required = false, defaultValue = "1", value = "pid") Long pid,
                             @RequestParam(required = false, value = "userName") String userName,
                             @RequestParam(required = false, value = "messageType") Integer messageType,
                             @RequestParam(required = false, value = "userType") Integer userType
    ) {
        BusinessUser businessUser = getCurrentUser();
        /*Integer userType = businessUser.getUserType();
        if (0 != userType && 2 != userType) {
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "该功能只提供给企业管理员和调度员！");
        }*/
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        recordManagerVo.setSendName(name);
        recordManagerVo.setMessageType(messageType);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user != null) {
                Long bid = user.getBid();
                recordManagerVo.setSendBid(bid);
            } else {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
        }
        recordManagerVo.setStartTime(startTime);
        recordManagerVo.setEndTime(endTime);
        recordManagerVo.setPid(pid);
        recordManagerVo.setGroupId(groupId);
        recordManagerVo.setUserType(userType);

        recordManagerVo.setType(RecordTypeEnum.talkBackDetail.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        PageInfo<RecordManagerVo> list = recordManagerService.selectRecord(page, limit, recordManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


    /**
     * 按条件导出对讲记录明细
     * 2020-3-12
     *
     * @param startTime
     * @param endTime
     * @param name        用户名
     * @param groupId     对讲组
     * @param pid         项目id
     * @param userName    账号
     * @param messageType 消息类型（1:终端 2:调度员）
     * @param userType
     * @return
     */
    @RequestMapping(value = "/exportTalkBackDetail", method = RequestMethod.POST)
    @RequiresPermissions("talkBackRecord:export")
    public Object exportTalkBackDetail(
            @RequestParam(required = false, value = "startTime") String startTime,
            @RequestParam(required = false, value = "endTime") String endTime,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, defaultValue = "1", value = "groupId") Long groupId,
            @RequestParam(required = false, defaultValue = "1", value = "pid") Long pid,
            @RequestParam(required = false, value = "userName") String userName,
            @RequestParam(required = false, value = "messageType") Integer messageType,
            @RequestParam(required = false, value = "userType") Integer userType,
            HttpServletResponse response
    ) {
        BusinessUser businessUser = getCurrentUser();
        /*Integer userType = businessUser.getUserType();
        if (0 != userType && 2 != userType) {
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "该功能只提供给企业管理员和调度员！");
        }*/
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        recordManagerVo.setSendName(name);
        recordManagerVo.setMessageType(messageType);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user != null) {
                Long bid = user.getBid();
                recordManagerVo.setSendBid(bid);
            } else {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
        }
        recordManagerVo.setStartTime(startTime);
        recordManagerVo.setEndTime(endTime);
        recordManagerVo.setPid(pid);
        recordManagerVo.setGroupId(groupId);
        recordManagerVo.setUserType(userType);

        recordManagerVo.setType(RecordTypeEnum.talkBackDetail.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        List<TalkBackRecordExcelVo> talkBackRecordExcelVos = recordManagerService.exportTalkBackRecord(recordManagerVo);
        iExcelService.exportExcel(talkBackRecordExcelVos, TalkBackRecordExcelVo.class, RecordTypeEnum.talkBackDetail.getName(), response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 导出对讲记录明细excel
     *
     * @return
     */
    @RequestMapping(value = "/exportTalkBackExcel", method = RequestMethod.POST)
    @RequiresPermissions("talkBackRecord:export")
    @Deprecated
    public Object exportTalkBackExcel(HttpServletResponse response, String ids) {
        logger.info("导出对讲记录明细excel");
        List<Long> list;
        if (ids == null || "".equals(ids)) {
            list = null;
        } else {
            list = JSONArray.parseArray(ids).toJavaList(Long.class);
        }
        List<TalkBackRecordExcelVo> listC = recordManagerService.exportTalkBackRecord(list);
        iExcelService.exportExcel(listC, TalkBackRecordExcelVo.class, RecordTypeEnum.talkBackDetail.getName(), response);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }
}
