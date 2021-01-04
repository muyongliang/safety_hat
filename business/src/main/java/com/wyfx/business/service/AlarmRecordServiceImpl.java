package com.wyfx.business.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.AlarmRecordMapper;
import com.wyfx.business.dao.AlarmTypeMapper;
import com.wyfx.business.dao.DeviceInfoMapper;
import com.wyfx.business.entity.AlarmRecord;
import com.wyfx.business.entity.AlarmType;
import com.wyfx.business.entity.DeviceInfo;
import com.wyfx.business.entity.vo.AlarmRecordExcelVo;
import com.wyfx.business.entity.vo.AlarmRecordVo;
import com.wyfx.business.entity.vo.AlarmTypeVo;
import com.wyfx.business.utils.MsgPushUtils;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlarmRecordServiceImpl implements AlarmRecordService {
    private static final Logger logger = LoggerFactory.getLogger(AlarmRecordServiceImpl.class);

    @Autowired
    private AlarmRecordMapper alarmRecordMapper;
    @Autowired
    private AlarmTypeMapper alarmTypeMapper;
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    /**
     * 删除选中记录
     *
     * @param ids
     */
    @Override
    public void deleteRecord(String ids) {
        List<Long> list = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        for (Long id : list) {
            alarmRecordMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除所有记录
     */
    @Override
    public void deleteAll() {
        alarmRecordMapper.deleteAll();
    }

    @Override
    public void updateViewStatus(Long id) {
        AlarmRecord alarmRecord = new AlarmRecord();
        alarmRecord.setView(true);
        alarmRecord.setId(id);
        alarmRecordMapper.updateByPrimaryKeySelective(alarmRecord);

    }

    /**
     * 添加记录
     *
     * @param alarmRecord
     */
    @Override
    public void addRecord(AlarmRecord alarmRecord) {
        alarmRecord.setRecordTime(new Date());
        alarmRecord.setView(false);
        alarmRecordMapper.insertSelective(alarmRecord);
        //-- 推送消息
        AlarmType alarmType = alarmTypeMapper.selectByPrimaryKey(alarmRecord.getAid());
        try {
            //推送报警信息给移动客户端
            MsgPushUtils.pushMessage(MsgPushUtils.buildPushObject_android_and_iosByTag(String.valueOf(alarmRecord.getPid()), alarmRecord.getAid().intValue(), alarmType.getType(), alarmType.getTipMessage()));
            //推送报警消息到前端
            AlarmTypeVo alarmTypeVo = new AlarmTypeVo();
            BeanUtils.copyProperties(alarmType, alarmTypeVo);
            alarmTypeVo.setRecordTime(alarmRecord.getRecordTime());
            alarmTypeVo.setSendName(alarmRecord.getSendName());
            DeviceInfo deviceInfo = deviceInfoMapper.selectDetailByBid(alarmRecord.getBid());
            String number = (deviceInfo == null) ? "" : deviceInfo.getNumber();
            alarmTypeVo.setNumber(number);
            String message = JSON.toJSONString(new BaseCommand(WsConstant.getAlarmRecord.name(), "", alarmTypeVo));
            WebSocketServer.sendAllMessage(message, UserTypeAndStatus.DISPATCHER, alarmRecord.getPid(), "web", null);
        } catch (Exception e) {
            logger.error("极光推送失败", e);
        }
    }

    /**
     * 查询报警记录
     *
     * @param page
     * @param limit
     * @param alarmRecordVo
     * @return
     */
    @Override
    public PageInfo<AlarmRecordVo> selectRecord(Integer page, Integer limit, AlarmRecordVo alarmRecordVo) {
        PageHelper.startPage(page, limit);
        List<AlarmRecordVo> recordVos = alarmRecordMapper.selectRecord(alarmRecordVo);
        return new PageInfo<>(recordVos);
    }

    /**
     * 根据报警类型查询所有未读的报警信息
     * aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警 -1所有报警
     * alarm_type 表中的aid
     *
     * @return
     */
    @Override
    public Map selectRecordStatusByAid(Long type) {
        logger.info("根据报警类型查询所有未读的报警信息 aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警 -1所有报警=" + type);
        List<Long> list;
        switch (type.intValue()) {
            case 1:
                list = alarmRecordMapper.selectUnreadRecordByAid(type);
                break;
            case 2:
                list = alarmRecordMapper.selectUnreadRecordByAid(type);
                break;
            case 3:
                list = alarmRecordMapper.selectUnreadRecordByAid(type);
                break;
            case 4:
                list = alarmRecordMapper.selectUnreadRecordByAid(type);
                break;
            case 5:
                list = alarmRecordMapper.selectUnreadRecordByAid(type);
                break;
            default:
                list = alarmRecordMapper.selectUnreadRecordByAid(null);
                break;
        }
        Map map = new HashMap();
        map.put("list", list);
        return map;
    }

    /**
     * 根据类型批量更新报警记录阅读状态 aid alarm_type 中的aid
     *
     * @param type aid 1 SOS报警 2围栏报警 3跌落报警 4脱离报警 5高空报警
     * @param ids  alarm_record 中的id集合
     * @return
     */
    @Override
    public boolean updateRecordStatusByAidsAndType(Long type, List<Long> ids) {
        int i = (ids.size() == 0) ? alarmRecordMapper.updateByIdsAndType(type, null) : alarmRecordMapper.updateByIdsAndType(type, ids);
        return i != 0;
    }

    /**
     * 根据报警类型查询报警记录
     *
     * @param pid
     * @param aid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<AlarmRecordVo> selectRecordByAid(Long pid, Long aid, Long businessId, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<AlarmRecordVo> recordVos = alarmRecordMapper.selectRecordByAid(pid, aid, businessId);
        return new PageInfo<>(recordVos);
    }

    /**
     * 查询所有报警记录并导出
     *
     * @return
     */
    @Override
    public List<AlarmRecordExcelVo> exportExcelDate(List<Long> ids) {
        List<AlarmRecordExcelVo> recordVos = alarmRecordMapper.exportExcelRecord(ids);
        return recordVos;
    }

    /**
     * 根据条件导出所有报警记录  2020-3-12
     *
     * @param alarmRecordVo 条件集合
     * @return
     */
    @Override
    public List<AlarmRecordVo> selectRecordToExcel(AlarmRecordVo alarmRecordVo) {
        List<AlarmRecordVo> alarmRecordVos = alarmRecordMapper.selectRecord(alarmRecordVo);
        return alarmRecordVos;
    }
}
