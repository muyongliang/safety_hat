package com.wyfx.total.service;

import com.wyfx.total.entity.AppManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IAppManagerService {

    boolean addAppManager(AppManager appManager);

    List<Map> selectAllAppMessages();

    /**
     * 更加类型查询app数据
     *
     * @param type 1终端 2调度员
     * @return
     */
    Map selectByType(Long type, HttpServletRequest request);
}
