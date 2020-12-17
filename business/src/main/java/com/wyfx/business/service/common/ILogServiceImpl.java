package com.wyfx.business.service.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.dao.LogMapper;
import com.wyfx.business.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ILogServiceImpl implements ILogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public boolean addLog(Log log) {
        int i = logMapper.insertSelective(log);
        return i >= 0;
    }

    /**
     * 通过条件查询日志
     *
     * @param accountType 账号类型
     * @param actionType  操作类型
     * @param startTime   开始时间
     * @param endTime     接收时间
     * @param executor    操作者姓名
     * @return
     */
    @Override
    public PageInfo selectByCondition(Long businessId, Integer accountType, Integer actionType, Date startTime, Date endTime, String executor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> mapList = logMapper.selectByCondition(businessId, accountType, actionType, startTime, endTime, executor);
        PageInfo pageInfo = new PageInfo(mapList);
        return pageInfo;
    }


    /**
     * 导出日志记录
     * todo 暂时未用到
     *
     * @param logIds
     * @return
     */
    @Override
    public List<Log> exportLogByLogIds(List<Long> logIds) {
        List<Log> logs = logMapper.selectByLogIds(logIds);
        return logs;
    }


    /**
     * 导出所有日志
     *
     * @return
     */
    @Override
    public List<Log> exportLog(Long businessId) {
        List<Log> logs = logMapper.selectAll(businessId);
        return logs;
    }


    /**
     * 通过用户id查询该用户所有日志
     * 用户可能是调度员也可以是终端或者企业管理员
     *
     * @param bid
     * @return
     */
    @Override
    public List<Log> exportLogByBid(Long bid) {
        List<Log> logs = logMapper.selectByBid(bid);
        return logs;
    }

    @Override
    public List<Map> getLogType() {
        OperationType[] operationTypes = OperationType.values();
        List<Map> list = new ArrayList<>();
        Map<String, String> map = null;
        for (OperationType operationType : operationTypes) {
            map = new HashMap<>(1);
            map.put("type", operationType.getType().toString());
            map.put("name", operationType.getName());
            list.add(map);
        }
        return list;
    }
}
