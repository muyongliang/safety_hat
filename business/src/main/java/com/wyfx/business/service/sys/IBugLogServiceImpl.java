package com.wyfx.business.service.sys;

import com.wyfx.business.dao.BugLogMapper;
import com.wyfx.business.entity.BugLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IBugLogServiceImpl implements IBugLogService {

    @Autowired
    private BugLogMapper bugLogMapper;

    @Override
    public boolean addBugLog(BugLog bugLog) {
        int i = bugLogMapper.insertSelective(bugLog);
        return i >= 0;
    }

    /**
     * 查询某个app的崩溃日志
     *
     * @param businessId 用户在business_user 中的bid
     * @return
     */
    @Override
    public List<String> selectSomeOneAllBugLog(Integer businessId) {
        List<BugLog> bugLogs = bugLogMapper.selectBugLogListByBusinessId(businessId);
        List<String> respList = new ArrayList<>();
        for (BugLog bugLog : bugLogs) {
            respList.add(bugLog.getContent());
        }
        return respList;
    }
}
