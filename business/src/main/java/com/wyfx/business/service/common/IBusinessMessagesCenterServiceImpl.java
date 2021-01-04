package com.wyfx.business.service.common;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.BusinessMessageCenterMapper;
import com.wyfx.business.entity.BusinessMessageCenter;
import com.wyfx.business.entity.vo.AlarmTypeVo;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by wsm on 2019-12-10
 * 用于总后台向企业后台推送消息
 */
@Service
@Transactional
public class IBusinessMessagesCenterServiceImpl implements IBusinessMessagesCenterService {
    private final Logger logger = LoggerFactory.getLogger(IBusinessMessagesCenterServiceImpl.class);
    @Autowired
    private BusinessMessageCenterMapper businessMessageCenterMapper;

    /**
     * 总后台推送消息添加
     *
     * @param messageCenter
     * @return
     */
    @Override
    public boolean addBusinessMessages(BusinessMessageCenter messageCenter) {
        int i = businessMessageCenterMapper.insertSelective(messageCenter);
        if (i < 0) {
            return false;
        }
        AlarmTypeVo alarmTypeVo = new AlarmTypeVo();
        alarmTypeVo.setRecordTime(messageCenter.getTime());
        alarmTypeVo.setTipMessage(messageCenter.getTitle());
        alarmTypeVo.setType("官方消息");
        alarmTypeVo.setAid(6L);
        String message = JSON.toJSONString(new BaseCommand(WsConstant.getAlarmRecord.name(), "", alarmTypeVo));
        WebSocketServer.sendAllMessage(message, UserTypeAndStatus.BUSINESS_MANAGER, "web", null);
        logger.info("推送官方消息成功");
        return true;
    }

    /**
     * 查询所有消息
     * 默认查询所有未读消息
     *
     * @return
     */
    @Override
    public Map selectAllBusinessMessages(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        //消息读取状态{0:未读取;1:已读取}
        List<Map> businessMessageCenters = businessMessageCenterMapper.selectAllMessagesByFlagAndType(null, null);
        PageInfo pageInfo = new PageInfo(businessMessageCenters);
        Map respMap = new HashMap(3);
        respMap.put("data", pageInfo);
        return respMap;
    }

    /**
     * 改变消息的查看状态为已读  消息读取状态{0:未读取;1:已读取}
     *
     * @param mids
     * @return
     */
    @Override
    public boolean updateBusinessMessagesFlagByMid(List<Long> mids) {
        if (mids.size() != 0) {
            int i = businessMessageCenterMapper.updateBusinessMessageFlag(mids);
            return i != 0;
        }
        return true;
    }

    /**
     * 查询是否存在未读消息
     *
     * @return 所有未读消息的id集合
     */
    @Override
    public List<Long> selectAllUnReadMessages() {
        List<Map> maps = businessMessageCenterMapper.selectAllMessagesByFlagAndType(0, null);
        List<Long> list = new ArrayList<>();
        for (Map map : maps) {
            long mid = Long.parseLong(map.get("mid").toString());
            list.add(mid);
        }
        return list;
    }
}
