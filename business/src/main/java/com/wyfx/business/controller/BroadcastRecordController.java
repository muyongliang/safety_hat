package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.constant.RecordTypeEnum;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.BroadcastRecordExcelVo;
import com.wyfx.business.entity.vo.RecordManagerVo;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author HQH
 * @date 2019/12/9
 * @description 广播记录
 */
@RestController
@RequestMapping("/broadcastRecord")
public class BroadcastRecordController extends BaseController {

    @Autowired
    private RecordManagerService recordManagerService;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private IExcelService iExcelService;

    /**
     * 添加广播记录
     *
     * @param recordManager sendBid 发送人
     *                      sendToBid 接受人 多个用，隔开
     *                      message 内容
     *                      messageType 消息类型 1文字 2语音
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Object addRecord(RecordManager recordManager) {
        BusinessUser businessUser = businessUserService.findByBid(recordManager.getSendBid());
        recordManager.setSendName(businessUser.getName());
        recordManager.setType(RecordTypeEnum.broadCast.getType());
        String bids = recordManager.getSendToBid();
        List<Long> list = Arrays.stream(bids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        StringBuilder names = new StringBuilder();
        for (Long bid : list) {
            String name = businessUserService.findByBid(bid).getName();
            names.append(name).append("/");
        }
        recordManager.setSendToName(names.substring(0, names.length() - 1));
        recordManagerService.addRecord(recordManager);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询广播记录明细
     *
     * @param id 广播id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions("broadcastRecord:detail")
    public Object detail(@PathVariable("id") Long id) {
        RecordManager recordManager = recordManagerService.selectDetail(id);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), recordManager);
    }

    /**
     * 删除指定记录
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("broadcastRecord:delete")
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
    @RequiresPermissions("broadcastRecord:clear")
    @AopLog(describe = "清空广播记录", operationType = OperationType.CLEAR)
    public Object clearRecord() {
        recordManagerService.deleteAll(RecordTypeEnum.broadCast.getType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 广播记录
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("broadcastRecord:view")
    public Object list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "startTime") String startTime,
                       @RequestParam(required = false, value = "endTime") String endTime,
                       @RequestParam(required = false, value = "name") String name,
                       @RequestParam(required = false, value = "userName") String userName
    ) {

        BusinessUser businessUser = getCurrentUser();
        Integer userType = null;
        try {
            userType = businessUser.getUserType();
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未登录");
        }
        /*if (0 != userType) {
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "该功能只提供给企业管理员！");
        }*/
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        if (!"".equals(name)) {
            recordManagerVo.setSendName(name);
        }

        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
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
        recordManagerVo.setType(RecordTypeEnum.broadCast.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());
        PageInfo<RecordManagerVo> list = recordManagerService.selectRecord(page, limit, recordManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


    /**
     * 按条件导出广播记录
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param userName
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportBroadcastRecord", method = RequestMethod.POST)
    @RequiresPermissions("broadcastRecord:export")
    public Object exportBroadcastRecord(
            @RequestParam(required = false, value = "startTime") String startTime,
            @RequestParam(required = false, value = "endTime") String endTime,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "userName") String userName,
            HttpServletResponse response
    ) {

        BusinessUser businessUser = getCurrentUser();
        Integer userType = null;
        try {
            userType = businessUser.getUserType();
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未登录");
        }
        /*if (0 != userType) {
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "该功能只提供给企业管理员！");
        }*/
        RecordManagerVo recordManagerVo = new RecordManagerVo();
        if (!"".equals(name)) {
            recordManagerVo.setSendName(name);
        }

        if (null != userName && !"".equals(userName)) {
            BusinessUser user = businessUserService.findByUserName(userName);
            if (user == null) {
                return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), new PageInfo<RecordManagerVo>());
            }
            Long bid = user.getBid();
            recordManagerVo.setSendBid(bid);
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
        recordManagerVo.setType(RecordTypeEnum.broadCast.getType());
        recordManagerVo.setBusinessId(businessUser.getBusinessId());

        List<BroadcastRecordExcelVo> listC = recordManagerService.exportBroadcastDate(recordManagerVo);
        iExcelService.exportExcel(listC, BroadcastRecordExcelVo.class, RecordTypeEnum.broadCast.getName(), response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 导出广播记录excel
     *
     * @return
     */
    @RequestMapping(value = "/exportBroadcastRecordExcel", method = RequestMethod.POST)
    @RequiresPermissions("broadcastRecord:export")
    @Deprecated
    public Object exportBroadcastRecordExcel(HttpServletResponse response, String ids) {
        List<Long> list;
        if (ids == null || "".equals(ids)) {
            list = null;
        } else {
            list = JSONArray.parseArray(ids).toJavaList(Long.class);
        }
        List<BroadcastRecordExcelVo> listC = recordManagerService.exportBroadcastRecord(list);
        iExcelService.exportExcel(listC, BroadcastRecordExcelVo.class, RecordTypeEnum.broadCast.getName(), response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "OK");
    }
}
