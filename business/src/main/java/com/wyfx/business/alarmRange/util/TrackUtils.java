package com.wyfx.business.alarmRange.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.alarmRange.api.IllegalTrackArgumentException;
import com.wyfx.business.alarmRange.model.BaseRequest;
import com.wyfx.business.alarmRange.model.LatLng;

import java.util.Map;

/**
 * track工具类
 *
 * @author baidu
 */
public class TrackUtils {

    /**
     * 检查经纬度
     *
     * @param latLng
     * @return
     */
    public static boolean checkLatLng(LatLng latLng) {
        if (null == latLng) {
            throw new IllegalTrackArgumentException("latLng can not be null");
        }
        if (Math.abs(latLng.getLatitude()) > 90 || Math.abs(latLng.getLongitude()) > 180) {
            throw new IllegalTrackArgumentException("latLng is invalid value");
        }
        return true;
    }

    /**
     * 检查经纬度
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    public static boolean checkLatLng(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalTrackArgumentException("latLng can not be null");
        }
        if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
            throw new IllegalTrackArgumentException("latLng is invalid value");
        }
        return true;
    }

    /**
     * 构造Http请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> packHttpRequestParams(BaseRequest request) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(request);
        String jsonStr = jsonObject.toJSONString().replace("serviceId", "service_id");
        Map map = JSON.parseObject(jsonStr, Map.class);
        return map;
    }


}
