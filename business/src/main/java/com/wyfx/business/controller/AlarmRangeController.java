package com.wyfx.business.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.AlarmRange;
import com.wyfx.business.entity.AlarmRecord;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.*;
import com.wyfx.business.service.AlarmRecordService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.trace.AlarmRangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 电子围栏
 */
@RestController
@RequestMapping("/alarmRange")
public class AlarmRangeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AlarmRangeController.class);
    private static final String sourceSign = "baidu_yingyan";
    private static final String encodingFlag = "gzip";
    @Autowired
    private AlarmRangeService alarmRangeService;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private AlarmRecordService alarmRecordService;
    @Autowired
    private IExcelService iExcelService;

    /**
     * 接收鹰眼服务端的报警推送
     *
     * @return
     */
    @RequestMapping(value = "/receiveAlarmInfo", method = RequestMethod.POST)
    public Object receiveAlarmInfo(HttpServletRequest request, HttpServletResponse response) {
        String signId = request.getHeader("SignId");
        String encoding = request.getHeader("Content-Encoding");
        logger.info("开始接收并处理百度鹰眼电子围栏报警信息：" + encoding);
        response.setHeader("SignId", sourceSign);
        Map<String, Object> responseMap = new HashMap<>();
        if (!sourceSign.equals(signId)) {
            responseMap.put("status", 1);
            responseMap.put("message", "未知来源");
            return responseMap;
        }
        BaiduAlarmResponse baiduAlarmResponse = null;
        try {
            BufferedReader bufferedReader = (encoding != null && encodingFlag.equals(encoding)) ? new BufferedReader(new InputStreamReader(new GZIPInputStream(request.getInputStream()), StandardCharsets.UTF_8)) : request.getReader();
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
            }
            bufferedReader.close();
            logger.info("接收到的数据:" + stringBuffer.toString());
            baiduAlarmResponse = JSON.parseObject(stringBuffer.toString()).toJavaObject(BaiduAlarmResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("status", 1);
            responseMap.put("message", "失败");
            return responseMap;
        }
        if (baiduAlarmResponse != null && baiduAlarmResponse.getType() == 2) {
            //将报警信息存入库中
            alarmRangeService.addBaiduAlarmInfo(baiduAlarmResponse);
        }
        responseMap.put("status", 0);
        responseMap.put("message", "成功");
        logger.info("接收百度鹰眼电子围栏报警成功:" + baiduAlarmResponse);
        return responseMap;
    }

    /**
     * 查询当前终端设备所在电子围栏的所有列表
     */
    @RequestMapping(value = "/getDeviceFenceRangeList", method = RequestMethod.GET)
    public Object getDeviceFenceRangeList() {
        BusinessUser user = getCurrentUser();
        List<AlarmRange> list = alarmRangeService.getDeviceFenceRangeList(user);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 编辑围栏时，查询围栏信息
     *
     * @param rid
     * @return
     */
    @RequestMapping(value = "/getFenceRangeInfoByRid", method = RequestMethod.GET)
    public Object getFenceRangeInfoByRid(Long rid) {
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), alarmRangeService.getAlarmRangeByRid(rid));
    }

    /**
     * 获取项目列表:
     * 调度员固定有电子围栏模块，仅有自己所在项目的数据权限
     *
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    public Object getProjectList() {
        BusinessUser businessUser = getCurrentUser();
        List<ProjectVo> list = alarmRangeService.getProjectList(businessUser.getBusinessId(), businessUser.getBid(), businessUser.getUserType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 获取当前用户下某一项目的电子围栏列表:
     * 调度员固定有电子围栏模块，仅有自己所在项目的数据权限
     *
     * @return
     */
    @RequestMapping(value = "/getFenceRangeList", method = RequestMethod.GET)
    public Object getFenceRangeList(Long projectId, Integer pageSize, Integer pageNum) {
        try {
            return getFenceRangeListByParam(projectId, null, null, pageSize, pageNum);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }

    }

    /**
     * @param projectId
     * @param type      围栏类型
     * @param name      围栏名称
     * @return
     */
    @RequestMapping(value = "/getFenceRangeListByParam", method = RequestMethod.POST)
    public Object getFenceRangeListByParam(Long projectId, Integer type, String name, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        if ("".equals(name)) {
            name = null;
        }
        if (businessUser.getUserType() == 1 && projectId == null) {
            projectId = businessUser.getProjectId();
        }
        try {
            PageInfo<RangeVo> pageInfo = alarmRangeService.findRangeByProjectId(businessUser.getBusinessId(), projectId, type, name, pageSize, pageNum);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }

    }

    /**
     * 根据电子围栏名称查询
     *
     * @param rangeName
     * @return
     */
    @RequestMapping(value = "/getFenceRangeByName", method = RequestMethod.POST)
    public Object getFenceRangeByName(Long projectId, String rangeName, Integer pageSize, Integer pageNum) {
        return getFenceRangeListByParam(projectId, null, rangeName, pageSize, pageNum);
    }

    /**
     * 根据围栏类型查询项目下的围栏信息
     *
     * @param projectId
     * @param type      禁止类型{0:禁止进入;1:禁止离开;2:全部}
     * @return
     */
    @RequestMapping(value = "/getFenceRangeByType", method = RequestMethod.POST)
    public Object getFenceRangeByType(Long projectId, Integer type, Integer pageSize, Integer pageNum) {
        return getFenceRangeListByParam(projectId, type, null, pageSize, pageNum);
    }

    /**
     * 删除电子围栏
     *
     * @param rangeId   本地围栏数据Id
     * @param fence_ids 百度地图围栏Id
     * @return
     */
    @RequestMapping(value = "/deleteRange", method = RequestMethod.POST)
//    @RequiresPermissions("alarmRange:delete")
    public Object deleteRange(Long rangeId, Integer fence_ids) {
        alarmRangeService.deleteAlarmRange(rangeId, fence_ids);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 统一添加围栏接口
     *
     * @param alarmRangeVo
     * @return
     */
    @RequestMapping(value = "/addFence", method = RequestMethod.POST)
//    @RequiresPermissions("alarmRange:add")
    public Object addFence(@RequestBody AlarmRangeVo alarmRangeVo) {
        try {
            alarmRangeService.addAlarmRange(alarmRangeVo);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/editFence", method = RequestMethod.POST)
//    @RequiresPermissions("alarmRange:edit")
    public Object editFence(@RequestParam(required = false, value = "fence_id") Integer fence_id, @RequestBody AlarmRangeVo alarmRangeVo) {
        try {
            alarmRangeService.updateAlarmRange(fence_id, alarmRangeVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取电子围栏下的终端成员
     *
     * @param rid
     * @return
     */
    @RequestMapping(value = "/getMemberList", method = RequestMethod.GET)
    public Object getMemberList(Long rid, Long projectId) {
        RangeMemberVo rangeMemberVo = alarmRangeService.findByRid(rid, projectId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), rangeMemberVo);
    }

    /**
     * 电子围栏终端成员管理
     *
     * @param rangeMemberVo
     * @return
     */
    @RequestMapping(value = "/managerClient", method = RequestMethod.POST)
//    @RequiresPermissions("alarmRange:managerClient")
    public Object managerClient(Long projectId, @RequestBody RangeMemberVo rangeMemberVo) {
        alarmRangeService.updateRangeMember(projectId, rangeMemberVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取电子围栏中的在线终端信息
     *
     * @param fenceId 百度围栏Id
     * @return
     */
    @RequestMapping(value = "/getOnlineClientList", method = RequestMethod.POST)
    public Object getOnlineClientList(Long fenceId, Integer pageSize, Integer pageNum) {
        PageInfo<List<Map>> pageInfo = alarmRangeService.findRangeAlarmRecordByRid(fenceId, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 更新某一围栏下某一终端的打卡记录
     *
     * @param fenceId  电子围栏Id
     * @param userName 电子围栏管理终端Id,即终端账号
     * @param type     围栏报警类型(禁止类型{0:禁止进入;1:禁止离开})
     * @return
     */
    @RequestMapping(value = "/updateRangeAlarmRecord", method = RequestMethod.POST)
    public Object updateRangeAlarmRecord(int fenceId, String userName, int type) {
        BusinessUser user = getCurrentUser();
        if (!userName.equals(user.getUserName())) {
            user = businessUserService.findByUserName(userName);
        }
        alarmRangeService.updateRangeAlarmRecord(type, fenceId, user);
        //添加报警记录并发送报警信息
        AlarmRecord alarmRecord = new AlarmRecord(null, new Date(), 2L, false, user.getBid(), null, user.getProjectId(), user.getName());
        alarmRecordService.addRecord(alarmRecord);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 导出电子围栏当前在线终端信息
     * create by wsm on 2019-12-20
     *
     * @param rid 电子围栏Id
     * @return
     */
    @GetMapping("/exportRangeAlarmOnlineClient")
    public MyResponseEntity exportRangeAlarmOnlineClient(Long rid, HttpServletResponse response) {
        List<OnlineClientVo> list = alarmRangeService.findRangeAlarmRecordByRid(rid);
        iExcelService.exportExcel(list, OnlineClientVo.class, "在线终端列表", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }
}
