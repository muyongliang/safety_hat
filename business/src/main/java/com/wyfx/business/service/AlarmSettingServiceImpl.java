package com.wyfx.business.service;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.AlarmSettingMapper;
import com.wyfx.business.entity.AlarmSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author johnson liu
 * @date 2019/11/11
 * @description 报警设置业务类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AlarmSettingServiceImpl implements AlarmSettingService {

    @Autowired
    private AlarmSettingMapper alarmSettingMapper;

    /**
     * 更新报警设置
     *
     * @param alarmId
     * @param alarmSetting
     */
    @Override
    @Transactional
    public void updateSetting(Long alarmId, AlarmSetting alarmSetting) {
        if (alarmId == null) {
            alarmSettingMapper.insertSelective(alarmSetting);
            return;
        }
        if (alarmSetting.getHignWorkAlarm() == null || alarmSetting.getHignWorkAlarm().equals("")) {
            alarmSetting.setHignWorkAlarm(0);
        }
        alarmSettingMapper.updateByPrimaryKeySelective(alarmSetting);
        // 向终端发送消息
        String message = JSON.toJSONString(new BaseCommand(WsConstant.updateAlarmSetting.name(), "", alarmSetting));
        WebSocketServer.sendAllMessage(message, 2, alarmSetting.getProjectId(), "android", null);
    }

    @Override
    public AlarmSetting findByProjectId(Integer projectId) {
        AlarmSetting alarmSetting = alarmSettingMapper.findByProjectId(projectId.longValue());
        if (alarmSetting == null) {
            alarmSetting = new AlarmSetting();
        }
        return alarmSetting;
    }
}
