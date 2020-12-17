package com.wyfx.total.service;


import com.wyfx.total.entity.PageSetting;

import java.util.Map;

public interface ISystemService {


    boolean updatePageSetting(PageSetting pageSetting);

    Map selectPageSetting();

}
