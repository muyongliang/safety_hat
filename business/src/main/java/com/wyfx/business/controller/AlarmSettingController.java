package com.wyfx.business.controller;

import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.AlarmSetting;
import com.wyfx.business.service.AlarmSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author johnson liu
 * @date 2019/11/11
 * @description 报警设置控制器
 */
@RestController
@RequestMapping("/alarmSetting")
public class AlarmSettingController {

    @Autowired
    private AlarmSettingService alarmSettingService;

    /**
     * 更新报警设置
     *
     * @param alarmSetting
     * @return
     */
    @RequestMapping(value = "/updateSetting", method = RequestMethod.POST)
//    @RequiresPermissions("alarmSetting:update")
    public Object updateSetting(@RequestBody AlarmSetting alarmSetting) {
        alarmSettingService.updateSetting(alarmSetting.getAlarmId(), alarmSetting);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询报警设置
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/findSetting", method = RequestMethod.GET)
//    @RequiresPermissions("alarmSetting:view")
    public Object findSetting(Integer projectId) {
        if (projectId == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未选中项目");
        }
        AlarmSetting alarmSetting = alarmSettingService.findByProjectId(projectId);// 前端检查参数传递是否正确
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), alarmSetting);
    }


}
