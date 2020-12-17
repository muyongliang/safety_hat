package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.AlarmRecord;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.AlarmRecordExcelVo;
import com.wyfx.business.entity.vo.AlarmRecordVo;
import com.wyfx.business.service.AlarmRecordService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HQH
 * @date 2019/12/9
 * @description 报警记录
 */
@RestController
@RequestMapping("/alarmRecord")
public class AlarmRecordController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(AlarmRecordController.class);
    @Autowired
    private AlarmRecordService alarmRecordService;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IExcelService iExcelService;

    /**
     * 添加报警记录
     *
     * @param alarmRecord
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Object addRecord(AlarmRecord alarmRecord) {
        BusinessUser businessUser = businessUserService.findByBid(alarmRecord.getBid());
        alarmRecord.setPid(businessUser.getProjectId());
        alarmRecord.setSendName(businessUser.getName());
        alarmRecordService.addRecord(alarmRecord);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


    /**
     * 根据报警类型查看记录（报警模块使用）
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listByAid", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "20", value = "limit") int limit,
                       @RequestParam(required = true, defaultValue = "1", value = "aid") long aid,
                       @RequestParam(required = false, value = "beginTime") String beginTime,
                       @RequestParam(required = false, value = "endTime") String endTime,
                       @RequestParam(required = false, value = "userName") String userName) {
        try {
            BusinessUser businessUser = getCurrentUser();
            Long pid = businessUser.getProjectId();
            /*AlarmRecordVo alarmRecordvo = new AlarmRecordVo();
            alarmRecordvo.setPid(pid);
            alarmRecordvo.setAid(aid);
            alarmRecordvo.setSendName(userName);
            alarmRecordvo.setStartTime();*/
            PageInfo<AlarmRecordVo> list = alarmRecordService.selectRecordByAid(pid, aid, businessUser.getBusinessId(), page, limit);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "");
        }

    }


    /**
     * 设置报警记录已查看（报警模块使用）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/checkedRecord", method = RequestMethod.POST)
    public Object checkedRecord(Long id) {
        alarmRecordService.updateViewStatus(id);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 删除指定记录（报警记录模块使用）
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("alarmRecord:delete")
    public Object checkedRecord(String ids) {
        if (ids == null || "".equals(ids)) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "参数错误");
        }
        alarmRecordService.deleteRecord(ids);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 清空记录（报警记录模块使用）
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = RequestMethod.POST)
    @RequiresPermissions("alarmRecord:clear")
    @AopLog(describe = "清空报警记录", operationType = OperationType.CLEAR)
    public Object clearRecord() {
        alarmRecordService.deleteAll();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 报警记录（报警记录模块使用）
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("alarmRecord:view")
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "pid") Long pid,
                       @RequestParam(required = false, value = "aid") Long aid,
                       @RequestParam(required = false, value = "startTime") String startTime,
                       @RequestParam(required = false, value = "endTime") String endTime,
                       @RequestParam(required = false, value = "name") String name,
                       @RequestParam(required = false, value = "userName") String userName
    ) {
        BusinessUser businessUser = getCurrentUser();
        AlarmRecordVo alarmRecordvo = new AlarmRecordVo();
        pid = (pid == null && businessUser.getUserType() == 1) ? businessUser.getProjectId() : pid;
        alarmRecordvo.setBusinessId(businessUser.getBusinessId());
        alarmRecordvo.setPid(pid);
        alarmRecordvo.setAid(aid);
        alarmRecordvo.setSendName(name);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<AlarmRecordVo>());
            }
            Long bid = businessUserService.findByUserName(userName).getBid();
            alarmRecordvo.setBid(bid);
        }
        try {
            alarmRecordvo.setStartTime(DateUtil.completionDate(startTime, true));
            alarmRecordvo.setEndTime(DateUtil.completionDate(endTime, false));
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }

        PageInfo<AlarmRecordVo> list = alarmRecordService.selectRecord(page, limit, alarmRecordvo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 根据报警类型查询所有未读的报警信息
     * aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警 ""所有记录
     *
     * @return
     */
    @GetMapping("/handleUnreadAlarmRecordStatus")
    public MyResponseEntity handleUnreadAlarmRecordStatus(Integer type) {
        logger.info("分类查询 未读的报警信息类型=" + type);
        Map map = alarmRecordService.selectRecordStatusByAid(type == null || "".equals(type) ? -1 : type.longValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


    /**
     * 通过类型 批量更新报警记录阅读状态为已读
     *
     * @param type aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警 ""所有报警
     * @param ids  alarm_record中的id集合
     * @return
     */
    @PostMapping("/handleUpdateAlarmRecordType")
    public MyResponseEntity handleUpdateAlarmRecordType(String type, String ids) {
        logger.info("通过类型 批量更新报警记录阅读状态为已读" + type + "==" + ids);
        boolean b;
        if (type == null || "".equals(type)) {
            b = alarmRecordService.updateRecordStatusByAidsAndType(null, new ArrayList() {
            });
        } else {
            b = alarmRecordService.updateRecordStatusByAidsAndType(Long.parseLong(type), JSONArray.parseArray(ids).toJavaList(Long.class));
        }
        if (b) {
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "更新成功");
        }
        return new MyResponseEntity(505, "更新失败，联系管理员");
    }


    /**
     * 根据不同条件导出报警记录    2020-3-12
     *
     * @param pid       项目id
     * @param aid       报警类型  1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param name      用户名
     * @param userName  账号
     * @return
     */
    @PostMapping("/export")
    @RequiresPermissions("alarmRecord:export")
    public MyResponseEntity export(@RequestParam(required = false, value = "pid") Long pid,
                                   @RequestParam(required = false, value = "aid") Long aid,
                                   @RequestParam(required = false, value = "startTime") String startTime,
                                   @RequestParam(required = false, value = "endTime") String endTime,
                                   @RequestParam(required = false, value = "name") String name,
                                   @RequestParam(required = false, value = "userName") String userName,
                                   HttpServletResponse response) {
        logger.info("根据不同条件导出报警记录");
        BusinessUser businessUser = getCurrentUser();
        AlarmRecordVo alarmRecordvo = new AlarmRecordVo();
        pid = (pid == null && businessUser.getUserType() == 1) ? businessUser.getProjectId() : pid;
        alarmRecordvo.setBusinessId(businessUser.getBusinessId());
        alarmRecordvo.setPid(pid);
        alarmRecordvo.setAid(aid);
        alarmRecordvo.setSendName(name);
        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<AlarmRecordVo>());
            }
            Long bid = businessUserService.findByUserName(userName).getBid();
            alarmRecordvo.setBid(bid);
        }
        try {
            alarmRecordvo.setStartTime(DateUtil.completionDate(startTime, true));
            alarmRecordvo.setEndTime(DateUtil.completionDate(endTime, false));
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }
        //导出报警记录
        List<AlarmRecordVo> alarmRecordVos = alarmRecordService.selectRecordToExcel(alarmRecordvo);
        System.err.println("导出报警记录的列表==" + alarmRecordVos);
        iExcelService.exportExcel(alarmRecordVos, AlarmRecordVo.class, "报警记录", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }


    /**
     * 导出excel（报警记录模块使用）
     *
     * @return
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    @RequiresPermissions("alarmRecord:export")
    @Deprecated
    public MyResponseEntity exportExcel(HttpServletResponse response, String ids) {
        List<Long> list;
        if (ids == null || "".equals(ids)) {
            list = null;
        } else {
            list = JSONArray.parseArray(ids).toJavaList(Long.class);
        }
        List<AlarmRecordExcelVo> listC = alarmRecordService.exportExcelDate(list);
        System.err.println("导出报警记录列表=" + listC);
        iExcelService.exportExcel(listC, AlarmRecordExcelVo.class, "报警记录", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }
}
