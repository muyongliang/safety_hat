package com.wyfx.business.service.trace;

import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.GpsVo;

public interface GpsService {
    void addGpsInfo(GpsVo gpsVo, BusinessUser user);


}
