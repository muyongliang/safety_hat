package com.wyfx.business.controller;

import com.wyfx.business.alarmRange.util.TrackUtils;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.GpsVo;
import com.wyfx.business.service.trace.GpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author johnson liu
 * @date 2019/11/27
 * @description 电子围栏
 */
@RestController
@RequestMapping("/gps")
@Api(value = "GpsController", tags = {"GPS控制器"})
public class GpsController extends BaseController {

    @Autowired
    private GpsService gpsService;

    /**
     * 上传终端采集的定位信息/气压等
     *
     * @return
     */
    @RequestMapping(value = "/uploadClientEnvInfo", method = RequestMethod.POST)
    @ApiOperation(value = "上传终端采集的定位信息/气压等", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object uploadClientEnvInfo(@RequestBody GpsVo gpsVo) {
        BusinessUser user = getCurrentUser();
        try {
            TrackUtils.checkLatLng(gpsVo.getLatitude(), gpsVo.getLongitude());//检查经纬度是否有效
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "错误的经纬度");
        }
        gpsService.addGpsInfo(gpsVo, user);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


}
