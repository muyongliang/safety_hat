package com.wyfx.total.service;

import com.wyfx.total.entity.DefaultSetting;

/**
 * 默认设置 即其他设置
 */
public interface IDefaultSettingService {

    boolean addDefaultSetting(DefaultSetting defaultSetting);

    boolean updateDefaultSetting(DefaultSetting defaultSetting);

    DefaultSetting findDefaultSetting();

}
