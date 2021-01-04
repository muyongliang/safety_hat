package com.wyfx.business.service.common;

import com.wyfx.business.dao.AppUpdateSettingMapper;
import com.wyfx.business.entity.AppUpdateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IAppUpdateServiceImpl implements IAppUpdateService {

    @Autowired
    private AppUpdateSettingMapper appUpdateSettingMapper;

    /**
     * 添加app更新设置
     *
     * @param appUpdateSetting
     * @return
     */
    @Override
    public boolean addAppUpdateSet(AppUpdateSetting appUpdateSetting) {
        int i = appUpdateSettingMapper.insertSelective(appUpdateSetting);
        return i >= 0;
    }

    /**
     * 编辑app更新设置
     *
     * @param appUpdateSetting
     * @return
     */
    @Override
    public boolean editAppUpdateSet(AppUpdateSetting appUpdateSetting) {
        int i;
        synchronized (this) {//避免总后台与企业后台同时修改数据
            i = appUpdateSettingMapper.updateByPrimaryKeySelective(appUpdateSetting);
        }
        return i >= 0;
    }

    /**
     * 查询app更新设置通过id
     *
     * @param appId
     * @return
     */
    @Override
    public AppUpdateSetting findAppSetById(Long appId) {
        AppUpdateSetting appUpdateSetting = appUpdateSettingMapper.selectByPrimaryKey(appId);
        return appUpdateSetting;
    }


}
