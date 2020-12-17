package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.LogInfo;
import com.wyfx.total.exception.InsertDataException;
import com.wyfx.total.mapper.LogInfoMapper;
import com.wyfx.total.service.ILogInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ILogInfServiceImpl implements ILogInfService {

    @Autowired
    private LogInfoMapper logInfoMapper;


    /**
     * 添加日志记录
     *
     * @param describe 操作描述
     * @param type     操作类型 /操作类型{0:增加;1:删除;2:更新}
     * @param data     操作的数据
     * @return
     */
    @Override
    @Transactional
    public boolean addLogInfRecord(String describe, Integer type, String data) {
        LogInfo logInfo = new LogInfo();
        logInfo.setExecutDescribe(describe);
        logInfo.setTaskType(type);
        logInfo.setRelatedData(data);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logInfo.setExecutor((String) request.getSession().getAttribute("userName"));
        logInfo.setCtime(new Date());
        int i = logInfoMapper.insertSelective(logInfo);
        if (i < 0) {
            throw new InsertDataException("添加日志失败,联系管理员。");
        }
        return true;
    }


    /**
     * 按条件查询日志
     *
     * @param taskType 类型  增 删 改    操作类型{0:增加;1:删除;2:更新}
     * @param startT
     * @param endT
     * @return
     */
    @Override
    public Map findLogInfoByCondition(Integer taskType, Date startT, Date endT, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<LogInfo> logInfos = logInfoMapper.selectLogInfByCondition(taskType, startT, endT);
        PageInfo pageInfo = new PageInfo(logInfos);
        Map map = new HashMap();
        map.put("logList", pageInfo);
        map.put("count", logInfoMapper.selectLogInfByCondition(taskType, startT, endT).size());
        return map;
    }

    /**
     * 查询所有的日志信息
     *
     * @return
     */
    @Override
    public Map findAllLogInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<LogInfo> logInfos = logInfoMapper.selectAll();
        PageInfo pageInfo = new PageInfo(logInfos);
        Map map = new HashMap();
        map.put("logInfo", pageInfo);
        map.put("count", logInfoMapper.selectAll().size());
        return map;
    }


    /**
     * 查询所有日志列表
     *
     * @return
     */
    @Override
    public List selectLogList() {
        List<LogInfo> logInfos = logInfoMapper.selectAll();
        return logInfos;
    }

    @Override
    public void exportBugLog(HttpServletResponse response) {

    }
}
