package com.wyfx.business.service.trace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.alarmRange.core.AlarmRangeHandler;
import com.wyfx.business.alarmRange.model.LatLng;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.controller.BaiduAlarmResponse;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.DataBean;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.*;
import com.wyfx.business.entity.*;
import com.wyfx.business.entity.vo.*;
import com.wyfx.business.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 电子围栏业务处理
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AlarmRangeServiceImpl implements AlarmRangeService {

    @Autowired
    private AlarmRangeMapper alarmRangeMapper;

    @Autowired
    private AlarmRangeMemberMapper alarmRangeMemberMapper;

    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private RangeAlarmRecordMapper rangeAlarmRecordMapper;
    @Autowired
    private BaiduAlarmMapper baiduAlarmMapper;
    @Autowired
    private AlarmTrackPointMapper alarmTrackPointMapper;

    @Value("${ak}")
    private String ak;
    @Value("${service_id}")
    private String serviceId;
    @Autowired
    private AlarmRecordMapper alarmRecordMapper;

    /**
     * 获取项目列表
     *
     * @param businessId
     * @param userType
     * @return
     */
    @Override
    public List<ProjectVo> getProjectList(Long businessId, Long bid, int userType) {
        List<ProjectVo> list = null;
        switch (userType) {
            case 0:
            case 3:
                //管理员
                list = alarmRangeMapper.selectByBusinessId(businessId);
                break;
            case 1:
                //调度员
                list = alarmRangeMapper.selectByBid(bid);
                break;
        }
        return list;
    }

    /**
     * 添加电子围栏
     *
     * @param alarmRangeVo
     */
    @Override
    @Transactional
    public void addAlarmRange(AlarmRangeVo alarmRangeVo) {
        String fence_name = alarmRangeVo.getName();
        List<LatLng> gpsRange = alarmRangeVo.getGpsRange();
        if (fence_name == null || fence_name.equals("") || gpsRange == null || gpsRange.size() < 1) {
            throw new SafetyHatException("创建围栏失败");
        }
        JSONObject result = null;
        try {
            //添加围栏成员
            AlarmRange alarmRange = new AlarmRange();
            BeanUtils.copyProperties(alarmRangeVo, alarmRange);
            alarmRange.setFenceId(alarmRangeVo.getFenceId());
            JSONArray jsonArray = (JSONArray) JSON.toJSON(alarmRangeVo.getGpsRange());
            alarmRange.setGpsRange(jsonArray.toJSONString());
            alarmRange.setCreateTime(new Date());
            alarmRangeMapper.insertSelective(alarmRange);

            List<Integer> members = businessUserMapper.selectByProjectIdAndUserType(alarmRangeVo.getProjectId().intValue(), 2);
            List<AlarmRangeMember> memberList = new ArrayList<>();
            Long rid = alarmRange.getRid();
            for (Integer member : members) {
                AlarmRangeMember alarmRangeMember = new AlarmRangeMember(null, member.longValue(), rid, 0);
                memberList.add(alarmRangeMember);
            }
            if (!memberList.isEmpty()) {
                alarmRangeMemberMapper.insertByForeach(memberList);
            }
            String message = JSON.toJSONString(new BaseCommand(WsConstant.updateRange.name(), "", new DataBean()));
            WebSocketServer.sendAllMessage(message, 2, alarmRangeVo.getProjectId(), "android", null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafetyHatException("创建围栏失败");
        }
    }

    /**
     * 将百度围栏报警信息入库
     *
     * @param info
     */
    @Override
    @Transactional
    public void addBaiduAlarmInfo(BaiduAlarmResponse info) {
        int serviceId = info.getService_id();
        List<BaiduAlarmResponse.ContentBean> content = info.getContent();
        BaiduAlarm baiduAlarm = null;
        AlarmTrackPoint alarmTrackPoint = null;
        AlarmTrackPoint alarmTrackPointPre = null;

        for (BaiduAlarmResponse.ContentBean contentBean : content) {//BaiduAlarmMapper
            baiduAlarm = new BaiduAlarm();
            alarmTrackPointPre = new AlarmTrackPoint();
            BeanUtils.copyProperties(contentBean.getPre_point(), alarmTrackPointPre);
            alarmTrackPointPre.setCoordType(contentBean.getPre_point().getCoord_type());
            alarmTrackPointPre.setLocTime(new Date(contentBean.getPre_point().getLoc_time() * 1000L));
            alarmTrackPointPre.setCreateTime(new Date(contentBean.getPre_point().getCreate_time() * 1000L));
            alarmTrackPointMapper.insert(alarmTrackPointPre);

            alarmTrackPoint = new AlarmTrackPoint();
            BeanUtils.copyProperties(contentBean.getAlarm_point(), alarmTrackPoint);
            alarmTrackPoint.setCoordType(contentBean.getAlarm_point().getCoord_type());
            alarmTrackPoint.setLocTime(new Date(contentBean.getAlarm_point().getLoc_time() * 1000L));
            alarmTrackPoint.setCreateTime(new Date(contentBean.getAlarm_point().getCreate_time() * 1000L));
            alarmTrackPointMapper.insert(alarmTrackPoint);

            baiduAlarm.setPrePoint(alarmTrackPointPre.getTid());
            baiduAlarm.setAlarmPoint(alarmTrackPoint.getTid());
            baiduAlarm.setAction(contentBean.getAction());
            baiduAlarm.setFenceId(contentBean.getFence_id());
            baiduAlarm.setFenceName(contentBean.getFence_name());
            baiduAlarm.setMonitoredPerson(contentBean.getMonitored_person());
            baiduAlarm.setServiceId(serviceId);
            baiduAlarmMapper.insert(baiduAlarm);
        }

    }

    private void checkAddResult(JSONObject result, AlarmRangeVo alarmRangeVo) {
        if (AlarmRangeHandler.commonResponseVerify(result)) {
            //请求成功
            Integer fence_id = result.getInteger("fence_id");
            AlarmRange alarmRange = new AlarmRange();
            BeanUtils.copyProperties(alarmRangeVo, alarmRange);
            alarmRange.setFenceId(fence_id);
            JSONArray jsonArray = (JSONArray) JSON.toJSON(alarmRangeVo.getGpsRange());
            alarmRange.setGpsRange(jsonArray.toJSONString());
            alarmRangeMapper.insertSelective(alarmRange);
        } else {
            throw new SafetyHatException("创建围栏失败");
        }
    }

    private void checkUpdateResult(JSONObject result, AlarmRangeVo alarmRangeVo) {
        if (AlarmRangeHandler.commonResponseVerify(result)) {
            //请求成功
            AlarmRange alarmRange = new AlarmRange();
            BeanUtils.copyProperties(alarmRangeVo, alarmRange);
            JSONArray jsonArray = (JSONArray) JSON.toJSON(alarmRangeVo.getGpsRange());
            alarmRange.setGpsRange(jsonArray.toJSONString());
            alarmRangeMapper.updateByPrimaryKey(alarmRange);
        } else {
            throw new SafetyHatException("创建围栏失败");
        }
    }

    /**
     * 更新电子围栏信息
     *
     * @param fence_id
     * @param alarmRangeVo
     */
    @Override
    @Transactional
    public void updateAlarmRange(Integer fence_id, AlarmRangeVo alarmRangeVo) {
        try {
            AlarmRange alarmRange = new AlarmRange();
            BeanUtils.copyProperties(alarmRangeVo, alarmRange);
            alarmRange.setUpdateTime(new Date());
            alarmRangeMapper.updateByPrimaryKeySelective(alarmRange);
            String message = JSON.toJSONString(new BaseCommand(WsConstant.updateRange.name(), "", new DataBean()));
            WebSocketServer.sendAllMessage(message, 2, alarmRangeVo.getProjectId(), "android", null);
        } catch (Exception e) {
            throw new SafetyHatException(e.getMessage());
        }
    }

    /**
     * 删除电子围栏
     *
     * @param rid 返回:{"status": 0,"message": "成功","fence_ids":1}
     */
    @Override
    @Transactional
    public void deleteAlarmRange(Long rid, Integer fence_ids) {
        alarmRangeMapper.deleteByFenceId(fence_ids);
    }

    /**
     * 更新成员状态为"已添加/未添加"
     *
     * @param rangeMemberVo rid 围栏ID
     *                      mids 成员ID
     *                      status 成员在群组的状态类型(0:未添加到群组;1已添加到群组)
     */
    @Override
    @Transactional
    public void updateRangeMember(Long projectId, RangeMemberVo rangeMemberVo) {
        List<AlarmRangeMember> members = new ArrayList<>();
        Long rid = rangeMemberVo.getRid();
        Integer type = null;
        AlarmRangeMember alarmRangeMember = null;
        for (ContactsVo contactsVo : rangeMemberVo.getAdd()) {
            //判断用户是否在围栏成员表中，如果不在，则添加
            alarmRangeMember = alarmRangeMemberMapper.selectByRidAndBid(rid, contactsVo.getId().longValue());
            if (alarmRangeMember == null) {
                alarmRangeMember = new AlarmRangeMember(null, contactsVo.getId().longValue(), rid, 1);
                alarmRangeMemberMapper.insert(alarmRangeMember);
                continue;
            }
            alarmRangeMember = new AlarmRangeMember(null, contactsVo.getId().longValue(), rid, 1);
            members.add(alarmRangeMember);
        }
        if (members.size() > 0) {
            alarmRangeMemberMapper.updateByForeach(rid, 1, members);
        }
        members.clear();
        for (ContactsVo contactsVo : rangeMemberVo.getWaitAdd()) {
            type = contactsVo.getType();
            if (type != null && type == 2) {
                alarmRangeMember = new AlarmRangeMember(null, contactsVo.getId().longValue(), rid, 0);
                members.add(alarmRangeMember);
            }
        }
        if (members.size() > 0) {
            alarmRangeMemberMapper.updateByForeach(rid, 0, members);
        }
        String message = JSON.toJSONString(new BaseCommand(WsConstant.updateRange.name(), "", new DataBean()));
        WebSocketServer.sendAllMessage(message, 2, projectId, "android", null);
    }

    @Override
    public AlarmRange getAlarmRangeByRid(Long rid) {
        if (rid == null || rid == 0) {
            throw new SafetyHatException("参数不能为null或0");
        }
        return alarmRangeMapper.selectByPrimaryKey(rid);
    }

    /**
     * 查询当前终端设备所在电子围栏列表
     *
     * @param user
     * @return
     */
    @Override
    public List<AlarmRange> getDeviceFenceRangeList(BusinessUser user) {
        return alarmRangeMapper.selectRangesByBid(1, user.getBid());
    }

    /**
     * 查询某电子围栏中的终端
     *
     * @param rid
     * @return
     */
    @Override
    public RangeMemberVo findByRid(Long rid, Long projectId) {
        RangeMemberVo rangeMemberVo = new RangeMemberVo();
        rangeMemberVo.setRid(rid);
        /*List<ContactsVo> waitList=alarmRangeMemberMapper.selectByRid(rid,0);*/
        List<ContactsVo> waitList = alarmRangeMemberMapper.selectNotInAlarmRangeClient(rid, 0, projectId);
        List<ContactsVo> addList = alarmRangeMemberMapper.selectByRid(rid, 1);
        if (waitList == null || waitList.size() < 1) {
            waitList = new ArrayList<>();
        }
        if (addList == null || addList.size() < 1) {
            addList = new ArrayList<>();
        }
        rangeMemberVo.setWaitAdd(waitList);
        rangeMemberVo.setAdd(addList);
        return rangeMemberVo;
    }

    /**
     * 查询某项目下的电子围栏
     *
     * @param projectId
     * @return
     */
    @Override
    public PageInfo<RangeVo> findRangeByProjectId(Long projectId, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RangeMemberVo> list = alarmRangeMapper.selectByProjectId(projectId.intValue());
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<RangeVo> findRangeByProjectId(Long businessId, Long projectId, Integer type, String name, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RangeMemberVo> list = alarmRangeMapper.selectByProjectIdAndTypeAndName(businessId, projectId, name, type);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 通过围栏名称查询围栏信息
     *
     * @param name
     * @return
     */
    @Override
    public PageInfo<RangeVo> findRangeByName(Long projectId, String name, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RangeMemberVo> list = alarmRangeMapper.selectByName(projectId.intValue(), name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 通过围栏类型查询围栏信息
     *
     * @param type
     * @return
     */
    @Override
    public PageInfo<RangeVo> findRangeByType(Long projectId, Integer type, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<RangeMemberVo> list = (type == 2) ? alarmRangeMapper.selectByProjectId(projectId.intValue()) : alarmRangeMapper.selectByType(projectId.intValue(), type);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 添加在线终端的打卡信息
     *
     * @param
     */
    @Override
    @Transactional
    public void updateRangeAlarmRecord(int type, int fenceId, BusinessUser user) {
        String currentDate = DateUtil.getCurrentDate();
        RangeAlarmRecord record = rangeAlarmRecordMapper.selectByDateAndFenceId(fenceId, currentDate);
        if (record == null) {
            //新增
            record = new RangeAlarmRecord(null, new Date(), null, null, user.getBid(), currentDate, fenceId);
            rangeAlarmRecordMapper.insertSelective(record);
            return;
        }
        record.setLastLeaveTime(new Date());
        rangeAlarmRecordMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据电子围栏Id查询终端打卡信息
     *
     * @param rid
     * @return
     */
    @Override
    public PageInfo<List<Map>> findRangeAlarmRecordByRid(Long rid, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Map> list = baiduAlarmMapper.selectOnlineClientByFenceId(rid);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }


    /**
     * 导出某电子围栏下的在线列表
     *
     * @param rid
     * @return
     */
    @Override
    public List<OnlineClientVo> findRangeAlarmRecordByRid(Long rid) {
        List<Map> list = rangeAlarmRecordMapper.selectOnlineClientByRid(rid);
        List<OnlineClientVo> respList = new ArrayList<>();
        for (Map map : list) {
            OnlineClientVo vo = new OnlineClientVo(
                    (String) map.get("name"), (String) map.get("mobile"), (String) map.get("work_Type"), (Date) map.get("first_into_time"), (Date) map.get("last_leave_time"));
            respList.add(vo);
        }
        return respList;
    }
}
