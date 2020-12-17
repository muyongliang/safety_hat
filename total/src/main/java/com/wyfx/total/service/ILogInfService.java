package com.wyfx.total.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ILogInfService {

    /**
     * 添加日志
     *
     * @param describe 操作描述
     * @param type     操作类型 /操作类型{0:增加;1:删除;2:更新}
     * @param data     操作的数据
     * @return
     */
    boolean addLogInfRecord(String describe, Integer type, String data);


    /**
     * 通过条件查询 操作日志
     *
     * @param taskType 类型 增 删 改
     * @param startT
     * @param endT
     * @return
     */
    Map findLogInfoByCondition(Integer taskType, Date startT, Date endT, Integer pageNum, Integer pageSize);

    Map findAllLogInfo(Integer pageNum, Integer pageSize);

    List selectLogList();

    void exportBugLog(HttpServletResponse response);

}
