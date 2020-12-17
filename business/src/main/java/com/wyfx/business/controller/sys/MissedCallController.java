package com.wyfx.business.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.MissedCalls;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.MissedCallServer;
import com.wyfx.business.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/12/11
 * @description
 */
@RestController
@RequestMapping("/missedCall")
public class MissedCallController extends BaseController {

    @Autowired
    private MissedCallServer missedCallServer;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IExcelService iExcelService;

    @RequestMapping(value = "/addMissRecord", method = RequestMethod.POST)
    public Object addMissRecord(String time, Integer type, String reason, String answerUserName) {
        if (type == null || reason == null || answerUserName == null || time == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "参数错误");
        }
        if ("".equals(time) || "".equals(reason) || "".equals(answerUserName) || type == 0) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "参数错误");
        }
        BusinessUser user = getCurrentUser();
        MissedCalls missedCalls = new MissedCalls();
        try {
            BusinessUser caller = businessUserService.findByUserName(answerUserName);
            missedCalls.setCaller(caller.getName());
            missedCalls.setTime(DateUtil.formatDate(time));
            missedCalls.setType(type);
            missedCalls.setReason(reason);
            missedCalls.setBid(caller.getBid());
            missedCalls.setIsView(false);
            missedCalls.setBusinessId(user.getBusinessId());
            missedCalls.setAnswerId(user.getBid());
            missedCallServer.addMissedCall(user.getUserName(), missedCalls);
        } catch (ParseException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "时间格式错误");
        } catch (Exception e) {
            System.out.println(e);
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "添加未接来电记录异常");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public Object clear() {
        BusinessUser user = getCurrentUser();
        if (user.getUserType() == 1) {
            missedCallServer.clearMissedCall(user.getBid(), user.getBusinessId());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(Long[] missId) {
        BusinessUser user = getCurrentUser();
        missedCallServer.deleteMissedCall(missId, user.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询企业未读未接来电记录的数量
     *
     * @return
     */
    @RequestMapping(value = "/getNotViewCount", method = RequestMethod.GET)
    public Object getNotViewCount() {
        BusinessUser user = getCurrentUser();
        List<Long> num = missedCallServer.getNotViewCount(user.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), num);
    }

    @RequestMapping(value = "/findMissedCalls", method = RequestMethod.POST)
    public Object findMissedCalls(Integer type, Integer pageSize, Integer pageNum) {
        BusinessUser user = getCurrentUser();
        PageInfo<MissedCalls> pageInfo = missedCallServer.findMissedCalls(user.getBid(), type, pageSize, pageNum, user.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    @RequestMapping(value = "/findMissedCallsByTime", method = RequestMethod.POST)
    public Object findMissedCallsByTime(String beginTime, String endTime, Integer pageSize, Integer pageNum) {
        BusinessUser user = getCurrentUser();
        PageInfo<MissedCalls> pageInfo = null;
        try {
            Date begin = (beginTime == null || "".equals(beginTime)) ? DateUtil.formatDate(DateUtil.getCurrentDate()) : DateUtil.formatDate(DateUtil.completionDate(beginTime));
            Date end = (endTime == null || "".equals(endTime)) ? new Date() : DateUtil.formatDate(DateUtil.completionDate(endTime));
            pageInfo = missedCallServer.findMissedCallsByTime(user.getBid(), begin, end, pageSize, pageNum, user.getBusinessId());
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 导出选中记录
     * create by wsm on 2019-12-17
     *
     * @param missIds
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public Object export(String missIds, HttpServletResponse response) {
        System.out.println(missIds);
        BusinessUser user = getCurrentUser();
        List<Long> list = JSONArray.parseArray(missIds).toJavaList(Long.class);
        System.out.println(list);
        List<MissedCalls> missedCalls = missedCallServer.exportMissedCalls(list, user.getBusinessId());
        iExcelService.exportExcel(missedCalls, MissedCalls.class, "未接来电记录", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public Object changeStatus(String ids) {
        if (ids == null || ids.equals("")) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "参数不能为空");
        }
        List<Integer> list = JSON.parseArray(ids).toJavaList(Integer.class);
        try {
            missedCallServer.updateStatusForeach(list);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "更改状态失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/getAllNotView", method = RequestMethod.GET)
    public Object getAllNotView() {

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


}
