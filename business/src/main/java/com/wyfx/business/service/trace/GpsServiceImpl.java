package com.wyfx.business.service.trace;

import com.wyfx.business.dao.GpsMapper;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Gps;
import com.wyfx.business.entity.vo.GpsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 定位业务处理类
 */
@Service
@Transactional
public class GpsServiceImpl implements GpsService {

    @Autowired
    private GpsMapper gpsMapper;

    @Override
    @Transactional
    public void addGpsInfo(GpsVo gpsVo, BusinessUser user) {
        Gps historyGps = gpsMapper.selectByClientId(user.getBid());

        Gps gps = (historyGps == null) ? new Gps() : historyGps;
        BeanUtils.copyProperties(gpsVo, gps);
        gps.setLatitude(gpsVo.getLatitude());
        gps.setLongitude(gpsVo.getLongitude());
        gps.setCreateTime(new Date());
        if (historyGps == null) {
            gps.setClientId(user.getBid());
            gpsMapper.insertSelective(gps);
        } else {
            gpsMapper.updateByPrimaryKeySelective(gps);
        }

    }
}
