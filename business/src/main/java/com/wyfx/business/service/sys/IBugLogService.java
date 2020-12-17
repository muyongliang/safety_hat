package com.wyfx.business.service.sys;

import com.wyfx.business.entity.BugLog;

import java.util.List;

/**
 * app 崩溃日志
 */
public interface IBugLogService {
    boolean addBugLog(BugLog bugLog);

    /**
     * 查询某个app用户的所有日志
     *
     * @param businessId
     * @return
     */
    List<String> selectSomeOneAllBugLog(Integer businessId);
}
