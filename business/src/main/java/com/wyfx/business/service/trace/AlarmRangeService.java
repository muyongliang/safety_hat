package com.wyfx.business.service.trace;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.controller.BaiduAlarmResponse;
import com.wyfx.business.entity.AlarmRange;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.*;

import java.util.List;
import java.util.Map;

public interface AlarmRangeService {
    List<ProjectVo> getProjectList(Long businessId, Long bid, int userType);

    void addAlarmRange(AlarmRangeVo alarmRangeVo);

    void addBaiduAlarmInfo(BaiduAlarmResponse info);

    void updateAlarmRange(Integer fence_id, AlarmRangeVo alarmRangeVo);

    void deleteAlarmRange(Long rid, Integer fence_ids);

    void updateRangeMember(Long projectId, RangeMemberVo rangeMemberVo);

    AlarmRange getAlarmRangeByRid(Long rid);

    /**
     * 查询当前终端设备所在电子围栏列表
     *
     * @param user
     * @return
     */
    List<AlarmRange> getDeviceFenceRangeList(BusinessUser user);

    /**
     * 查询某电子围栏中的终端
     *
     * @param rid
     * @return
     */
    RangeMemberVo findByRid(Long rid, Long projectId);

    /**
     * 查询某项目下的电子围栏
     *
     * @param projectId
     * @return
     */
    PageInfo<RangeVo> findRangeByProjectId(Long projectId, Integer pageSize, Integer pageNum);

    PageInfo<RangeVo> findRangeByProjectId(Long businessId, Long projectId, Integer type, String name, Integer pageSize, Integer pageNum);

    /**
     * 通过围栏名称查询围栏信息
     *
     * @param name
     * @return
     */
    PageInfo<RangeVo> findRangeByName(Long projectId, String name, Integer pageSize, Integer pageNum);

    /**
     * 通过围栏类型查询围栏信息
     *
     * @param type
     * @return
     */
    PageInfo<RangeVo> findRangeByType(Long projectId, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 更新在线终端的打卡信息
     *
     * @param
     */
    void updateRangeAlarmRecord(int type, int fenceId, BusinessUser user);

    /**
     * 根据电子围栏Id查询终端打卡信息
     *
     * @param rid
     * @return
     */
    PageInfo<List<Map>> findRangeAlarmRecordByRid(Long rid, Integer pageSize, Integer pageNum);


    /**
     * 导出在线终端信息
     *
     * @param rid
     * @return
     */
    List<OnlineClientVo> findRangeAlarmRecordByRid(Long rid);
}
