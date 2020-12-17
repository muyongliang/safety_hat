package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.constant.RecordTypeEnum;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.PhoneRecordExcelVo;
import com.wyfx.business.entity.vo.RecordManagerVo;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.DateUtil;
import com.wyfx.business.utils.FilePathUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author HQH
 * @date 2019/12/9
 * @description 电话记录
 */
@RestController
@RequestMapping("/phoneRecord")
public class PhoneRecordController extends BaseController {

    @Autowired
    private RecordManagerService recordManagerService;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private IExcelService iExcelService;

    /**
     * 添加电话录音记录
     *
     * @param recordManager （sendBid sendToBid title messageType）
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Object addRecord(RecordManager recordManager) {
        BusinessUser businessUser = businessUserService.findByBid(recordManager.getSendBid());
        recordManager.setSendName(businessUser.getName());
        recordManager.setType(RecordTypeEnum.phone.getType());
        String sendTo = recordManager.getSendToBid();
        String name = businessUserService.findByBid(Long.valueOf(sendTo)).getName();
        recordManager.setSendToName(name);
        recordManagerService.addRecord(recordManager);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/addRecordVo", method = RequestMethod.POST)
    public Object addRecord(RecordManagerVo recordManagerVo) {
        BusinessUser caller = businessUserService.findByUserName(recordManagerVo.getCallerAccount());
        String calledAccount = recordManagerVo.getCalledAccount();
        String[] calledAccounts = calledAccount.split(",");

        BusinessUser called = null;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBufferName = new StringBuffer();
        for (String account : calledAccounts) {
            called = businessUserService.findByUserName(account);
            stringBuffer.append(called.getBid());
            stringBuffer.append(",");
            stringBufferName.append(called.getName());
            stringBufferName.append(",");
        }

        if (caller == null || called == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "账号不存在");
        }
        RecordManager recordManager = new RecordManager();
        try {
            recordManager.setRecordTime(DateUtil.formatDate(recordManagerVo.getRecordTime()));
        } catch (Exception e) {
            throw new SafetyHatException("时间格式错误");
        }
        recordManager.setRecordBy(recordManagerVo.getRecordBy());
        recordManager.setPid(caller.getProjectId());
        recordManager.setSendBid(caller.getBid());
        recordManager.setSendName(caller.getName());
        recordManager.setSendToBid(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
        recordManager.setSendToName(stringBufferName.toString().substring(0, stringBufferName.toString().length() - 1));
        recordManager.setMessage(recordManagerVo.getMessage().substring(recordManagerVo.getMessage().indexOf("/safety-hat/")));
        recordManager.setMessageType(2);
        recordManager.setType(RecordTypeEnum.phone.getType());
        recordManager.setTitle(recordManagerVo.getTitle());
        recordManagerService.addRecord(recordManager);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询电话录音记录明细
     *
     * @param id 电话录音id
     * @return
     */

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions("phoneRecord:viewDetail")
    public Object detail(@PathVariable("id") Long id, HttpServletRequest request) {
        RecordManager recordManager = recordManagerService.selectDetail(id);
        String http = "http";
        String url = (recordManager.getMessage().contains(http)) ? recordManager.getMessage() : FilePathUtil.getBaseUrl(request) + recordManager.getMessage();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), url);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public Object download(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        String url = recordManagerService.selectDetail(id).getMessage();
        FilePathUtil.download(url, request, response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), url);
    }

    /**
     * 删除指定记录
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("phoneRecord:delete")
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
    @RequiresPermissions("phoneRecord:clear")
    @AopLog(describe = "清空电话记录：", operationType = OperationType.CLEAR)
    public Object clearRecord() {
        recordManagerService.deleteAll(RecordTypeEnum.phone.getType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 电话录音记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("phoneRecord:view")
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "startTime") String startTime,
                       @RequestParam(required = false, value = "endTime") String endTime,
                       @RequestParam(required = false, value = "name") String name,
                       @RequestParam(required = false, value = "userName") String userName,
                       @RequestParam(required = false, value = "toUserName") String toUserName,
                       @RequestParam(required = false, value = "toName") String toName
    ) {
        BusinessUser businessUser = getCurrentUser();
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        if (!"".equals(name)) {
            recordManagerVo.setSendName(name);
        }
        if (!"".equals(toName)) {
            recordManagerVo.setSendToName(toName);
        }
        BusinessUser user = null;
        if (null != userName && !"".equals(userName)) {
            user = businessUserService.findByUserName(userName);
            if (null == user) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
        }
        if (null != toUserName && !"".equals(userName)) {
            user = businessUserService.findByUserName(toUserName);
            if (null == user) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendToBid(bid);
        }
        try {
            if (!"".equals(startTime)) {
                recordManagerVo.setStartTime(DateUtil.completionDate(startTime, true));
            }
            if (!"".equals(endTime)) {
                recordManagerVo.setEndTime(DateUtil.completionDate(endTime, false));
            }
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }
        if (businessUser.getUserType() == 1) {
            recordManagerVo.setPid(businessUser.getProjectId());
        }
        recordManagerVo.setType(RecordTypeEnum.phone.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        PageInfo<RecordManagerVo> list = recordManagerService.selectPhoneRecord(page, limit, recordManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


    /**
     * 按条件导出电话录音记录
     *
     * @param startTime
     * @param endTime
     * @param name       发送人姓名
     * @param userName   发送人账号
     * @param toUserName 被叫人账号
     * @param toName     被叫人姓名
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportPhoneRecord", method = RequestMethod.POST)
    @RequiresPermissions(value = {"phoneRecord:export", "phoneRecord:download"})
    public Object exportPhoneRecord(
            @RequestParam(required = false, value = "startTime") String startTime,
            @RequestParam(required = false, value = "endTime") String endTime,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "userName") String userName,
            @RequestParam(required = false, value = "toUserName") String toUserName,
            @RequestParam(required = false, value = "toName") String toName,
            HttpServletResponse response
    ) {
        BusinessUser businessUser = getCurrentUser();
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        if (!"".equals(name)) {
            recordManagerVo.setSendName(name);
        }
        if (!"".equals(toName)) {
            recordManagerVo.setSendToName(toName);
        }
        BusinessUser user = null;
        if (null != userName && !"".equals(userName)) {
            user = businessUserService.findByUserName(userName);
            if (null == user) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
        }
        if (null != toUserName && !"".equals(userName)) {
            user = businessUserService.findByUserName(toUserName);
            if (null == user) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendToBid(bid);
        }
        try {
            if (!"".equals(startTime)) {
                recordManagerVo.setStartTime(DateUtil.completionDate(startTime, true));
            }
            if (!"".equals(endTime)) {
                recordManagerVo.setEndTime(DateUtil.completionDate(endTime, false));
            }
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "日期格式错误");
        }
        if (businessUser.getUserType() == 1) {
            recordManagerVo.setPid(businessUser.getProjectId());
        }
        recordManagerVo.setType(RecordTypeEnum.phone.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        List<PhoneRecordExcelVo> listC = recordManagerService.exportPhoneRecordDate(recordManagerVo);
        iExcelService.exportExcel(listC, PhoneRecordExcelVo.class, RecordTypeEnum.phone.getName(), response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 导出excel（电话录音记录）
     *
     * @return
     */
    @RequestMapping(value = "/exportPhoneRecordExcel", method = RequestMethod.POST)
    @RequiresPermissions(value = {"phoneRecord:export", "phoneRecord:download"})
    @Deprecated
    public Object exportExcel(HttpServletResponse response, String ids) {
        List<Long> list;
        if (ids == null || "".equals(ids)) {
            list = null;
        } else {
            list = JSONArray.parseArray(ids).toJavaList(Long.class);
        }
        List<PhoneRecordExcelVo> listC = recordManagerService.exportPhoneRecord(list);
        iExcelService.exportExcel(listC, PhoneRecordExcelVo.class, RecordTypeEnum.phone.getName(), response);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }
}
