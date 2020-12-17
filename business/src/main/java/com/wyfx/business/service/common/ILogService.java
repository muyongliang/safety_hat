package com.wyfx.business.service.common;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.Log;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志记录
 */
public interface ILogService {
    boolean addLog(Log log);

    PageInfo selectByCondition(Long businessId, Integer accountType, Integer actionType, Date startTime, Date endTime, String executor, Integer pageNum, Integer pageSize);

    List<Log> exportLogByLogIds(List<Long> logIds);

    List<Log> exportLog(Long businessId);

    List<Log> exportLogByBid(Long bid);

    List<Map> getLogType();
}
