package com.wyfx.business.service.common;

import com.wyfx.business.entity.AppUpdateSetting;

public interface IAppUpdateService {
    boolean addAppUpdateSet(AppUpdateSetting appUpdateSetting);

    boolean editAppUpdateSet(AppUpdateSetting appUpdateSetting);

    AppUpdateSetting findAppSetById(Long appId);


}
