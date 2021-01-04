package com.wyfx.business.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.RecordManagerMapper;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.*;
import com.wyfx.business.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordManagerServiceImpl implements RecordManagerService {

    @Autowired
    private RecordManagerMapper recordManagerMapper;


    /**
     * 删除选中记录
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteRecord(String ids) {
        List<Long> list = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        for (Long id : list) {
            recordManagerMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除所有记录
     */
    @Override
    public void deleteAll(Integer type) {
        recordManagerMapper.deleteAll(type);
    }

    /**
     * 根据组id和项目id删除对讲记录
     *
     * @param groupId
     * @param pid
     */
    @Override
    public void deleteByGroupIdAndPid(Long groupId, Long pid) {
        recordManagerMapper.deleteTalkBackRecords(groupId, pid);
    }


    /**
     * 添加记录
     *
     * @param recordManager
     */
    @Override
    public void addRecord(RecordManager recordManager) {
        if (recordManager.getRecordTime() == null) {
            recordManager.setRecordTime(new Date());
        }
        recordManagerMapper.insertSelective(recordManager);
    }

    /**
     * 查询记录
     *
     * @param page
     * @param limit
     * @param recordManagerVo
     * @return
     */
    @Override
    public PageInfo<RecordManagerVo> selectRecord(Integer page, Integer limit, RecordManagerVo recordManagerVo) {
        PageHelper.startPage(page, limit);
        List<RecordManagerVo> recordVos = null;
        if (recordManagerVo.getType() == 5) {
            //查询巡检报告
            recordVos = recordManagerMapper.selectPatrolRecord(recordManagerVo);
        } else {
            recordVos = recordManagerMapper.selectRecord(recordManagerVo);
        }
        return new PageInfo<>(recordVos);
    }

    /**
     * 查询电话记录
     *
     * @param page
     * @param limit
     * @param recordManagerVo
     * @return
     */
    @Override
    public PageInfo<RecordManagerVo> selectPhoneRecord(Integer page, Integer limit, RecordManagerVo recordManagerVo) {
        PageHelper.startPage(page, limit);
        List<RecordManagerVo> recordVos = recordManagerMapper.selectPhoneRecord(recordManagerVo);
        return new PageInfo<>(recordVos);
    }

    @Override
    public PageInfo<TalkBackRecordVo> selectTalkBackRecord(Integer page, Integer limit, TalkBackRecordVo talkBackRecordVo) {
        PageHelper.startPage(page, limit);
        List<TalkBackRecordVo> recordVos = recordManagerMapper.selectTalkBackRecord(talkBackRecordVo);
        return new PageInfo<>(recordVos);
    }


    /**
     * 查询广播记录并导出
     * <p>
     * 记录类型 1:广播记录 2:对讲记录 3:对讲记录明细 4:电话录音记录 5:巡检报告
     *
     * @return
     */
    @Override
    public List<BroadcastRecordExcelVo> exportBroadcastRecord(List<Long> ids) {
        List<BroadcastRecordExcelVo> recordVos = recordManagerMapper.exportBroadcastRecord(ids);
        return recordVos;
    }

    /**
     * 按条件导出广播记录
     * 记录类型 1:广播记录 2:对讲记录 3:对讲记录明细 4:电话录音记录 5:巡检报告
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    @Override
    public List<BroadcastRecordExcelVo> exportBroadcastDate(RecordManagerVo recordManagerVo) {
        List<BroadcastRecordExcelVo> broadcastRecordExcelVos = recordManagerMapper.exportBroadcastDate(recordManagerVo);
        return broadcastRecordExcelVos;
    }

    /**
     * 查询电话录音记录并导出
     *
     * @return
     */
    @Override
    public List<PhoneRecordExcelVo> exportPhoneRecord(List<Long> ids) {
        List<PhoneRecordExcelVo> recordVos = recordManagerMapper.exportPhoneRecord(ids);
        return recordVos;
    }

    /**
     * 按条件导出电话记录
     * 2020-3-12
     *
     * @param recordManagerVo
     * @return
     */
    @Override
    public List<PhoneRecordExcelVo> exportPhoneRecordDate(RecordManagerVo recordManagerVo) {
        List<PhoneRecordExcelVo> phoneRecordExcelVos = recordManagerMapper.exportPhoneRecordDate(recordManagerVo);
        return phoneRecordExcelVos;
    }

    /**
     * 查询巡检并导出
     *
     * @return
     */
    @Override
    public List<PatrolRecordExcelVo> exportPatrolRecord(List<Long> ids) {
        List<PatrolRecordExcelVo> recordVos = recordManagerMapper.exportPatrolRecord(ids);
        return recordVos;
    }

    /**
     * 按条件导出巡检报告
     * 2020-3-12
     *
     * @param alarmRecordVo
     * @return
     */
    @Override
    public List<PatrolRecordExcelVo> exportPatrolRecord(RecordManagerVo alarmRecordVo) {
        List<PatrolRecordExcelVo> patrolRecordExcelVos = recordManagerMapper.exportPatrolRecordDate(alarmRecordVo);
        return patrolRecordExcelVos;
    }

    /**
     * 导出对讲明细记录
     *
     * @param ids
     * @return
     */
    @Override
    public List<TalkBackRecordExcelVo> exportTalkBackRecord(List<Long> ids) {

        List<TalkBackRecordExcelVo> recordVos = recordManagerMapper.exportTalkBackRecord(ids);
        return recordVos;
    }

    /**
     * 按条件导出对讲记录
     * 2020-3-12
     *
     * @param alarmRecordVo
     * @return
     */
    @Override
    public List<TalkBackRecordExcelVo> exportTalkBackRecord(RecordManagerVo alarmRecordVo) {
        List<TalkBackRecordExcelVo> talkBackRecordExcelVos = recordManagerMapper.exportTalkBackRecordDate(alarmRecordVo);
        return talkBackRecordExcelVos;
    }

    /**
     * 查询指定记录明细
     *
     * @param id
     * @return
     */
    @Override
    public RecordManager selectDetail(Long id) {
        return recordManagerMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, String> getUrlById(Long id, HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            RecordManager recordManager = recordManagerMapper.selectByPrimaryKey(id);
            String reMessage = recordManager.getMessage();
            if (reMessage.contains("http://")) {
                String path = request.getServerPort() + File.separator + request.getContextPath();
                reMessage.substring(reMessage.indexOf(path) + path.length());
            }
            String message = (recordManager.getMessageType() == 2) ? FilePathUtil.getBaseUrl(request) + File.separator + reMessage : recordManager.getMessage();
            map.put("sendToName", recordManager.getSendToName());
            map.put("message", message);
        } catch (Exception e) {
            throw e;
        }
        return map;
    }
}
