package com.wyfx.total.service.impl;

import com.wyfx.total.entity.AppManager;
import com.wyfx.total.mapper.AppManagerMapper;
import com.wyfx.total.service.IAppManagerService;
import com.wyfx.total.utile.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wsm
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IAppManagerServiceImpl implements IAppManagerService {

    private static final Logger logger = LoggerFactory.getLogger(IAppManagerService.class);
    @Autowired
    private AppManagerMapper appManagerMapper;

    /**
     * 添加app更新
     *
     * @param appManager
     * @return
     */
    @Override
    public boolean addAppManager(AppManager appManager) {
        AppManager appManager1 = appManagerMapper.selectByPrimaryKey(appManager.getId());
        if (appManager1 != null) {
            appManagerMapper.updateByPrimaryKeySelective(appManager);
        } else {
            appManagerMapper.insertSelective(appManager);
        }
        return true;
    }

    /**
     * 查询所有的app信息
     *
     * @return
     */
    @Override
    public List<Map> selectAllAppMessages() {
        List<Map> mapList = appManagerMapper.selectAll();
        return mapList;
    }


    /**
     * 通过类型查询App数据  类型同时也代表app在数据库中的id
     * 数据库中只有两条数据库
     *
     * @param type 1终端 2调度员
     * @return
     */
    @Override
    public Map selectByType(Long type, HttpServletRequest request) {
        AppManager appManager = appManagerMapper.selectByPrimaryKey(type);
        Map map = new HashMap((int) (7 / 0.75F) + 1);
        //还未上传app
        if (appManager == null) {
            logger.info("还没有app更新数据,数据为空");
            map.put("version_code", "0");
            map.put("new_version", "0");
            map.put("apk_file_url", "0");
            map.put("update_log", "0");
            map.put("target_size", "0");
            map.put("new_md5", "0");
            map.put("cons", "0");
        } else {
            logger.info("app更新数据" + appManager);
            map.put("version_code", appManager.getVersion());
            map.put("new_version", appManager.getClientVersion());
            map.put("apk_file_url", FilePathUtil.getBaseUrl(request) + appManager.getUrl());
            map.put("update_log", appManager.getDescription());
            map.put("target_size", appManager.getTargetSize() + "M");
            map.put("new_md5", appManager.getNewMd5());
            map.put("cons", appManager.getCons() == 0 ? "false" : "true");
        }
        return map;
    }
}
