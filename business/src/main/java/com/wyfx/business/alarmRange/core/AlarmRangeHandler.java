package com.wyfx.business.alarmRange.core;

import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.alarmRange.LBSAlarmRangeClient;
import com.wyfx.business.alarmRange.api.AddCirclefenceRequest;
import com.wyfx.business.alarmRange.api.AddPolygonfenceRequest;
import com.wyfx.business.alarmRange.api.DeleteFenceRequest;
import com.wyfx.business.alarmRange.api.IllegalTrackArgumentException;
import com.wyfx.business.alarmRange.model.*;
import com.wyfx.business.alarmRange.util.RequestUtils;
import com.wyfx.business.alarmRange.util.TrackUtils;
import com.wyfx.business.controller.commons.SafetyHatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author johnson liu
 * @date 2019/11/29
 * @description AlarmRange百度api处理器
 */
public class AlarmRangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(AlarmRangeHandler.class);

    private static final AtomicLong mSequenceGenerator = new AtomicLong();

    /**
     * 添加圆形围栏
     * {"status": 0,"message": "成功","fence_id":1}
     *
     * @param request
     */
    public static JSONObject createCirclefence(AddCirclefenceRequest request) {
        Map<String, String> parameters = packRequest(request);
        String result = RequestUtils.sendRequest(UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        //AsyncRequestClient.getInstance().submitTask(mSequenceGenerator.incrementAndGet(), UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        System.out.println("调用百度地图api创建圆形:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * 添加多边形围栏
     * {"status": 0,"message": "成功","fence_id":1}
     *
     * @param request
     */
    public static JSONObject createpolygonfence(AddPolygonfenceRequest request) {
        Map<String, String> parameters = packRequest(request);
        String result = RequestUtils.sendRequest(UrlDomain.createPolygonfence, parameters, RequestUtils.METHOD_POST);
        //AsyncRequestClient.getInstance().submitTask(mSequenceGenerator.incrementAndGet(), UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        System.out.println("调用百度地图api创建多边形围栏:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static JSONObject updateCirclefence(Integer fence_id, AddCirclefenceRequest request) {
        Map<String, String> parameters = packRequest(request);
        parameters.put("fence_id", String.valueOf(fence_id));
        String result = RequestUtils.sendRequest(UrlDomain.updateCirclefence, parameters, RequestUtils.METHOD_POST);
        //AsyncRequestClient.getInstance().submitTask(mSequenceGenerator.incrementAndGet(), UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        System.out.println("调用百度地图api更新圆形围栏:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static JSONObject updatePolygonfence(Integer fence_id, AddPolygonfenceRequest request) {
        Map<String, String> parameters = packRequest(request);
        parameters.put("fence_id", String.valueOf(fence_id));
        String result = RequestUtils.sendRequest(UrlDomain.updatePolygonfence, parameters, RequestUtils.METHOD_POST);
        //AsyncRequestClient.getInstance().submitTask(mSequenceGenerator.incrementAndGet(), UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        System.out.println("调用百度地图api更新多边形围栏:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static JSONObject deleteFence(DeleteFenceRequest request) {
        Map<String, String> parameters = packRequest(request);
        String result = RequestUtils.sendRequest(UrlDomain.deleteFence, parameters, RequestUtils.METHOD_POST);
        //AsyncRequestClient.getInstance().submitTask(mSequenceGenerator.incrementAndGet(), UrlDomain.createCirclefence, parameters, RequestUtils.METHOD_POST);
        System.out.println("调用百度地图api删除围栏:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * 组装多边形围栏形状点参数值
     *
     * @param gpsRange
     * @return
     */
    public static String packVertexes(List<LatLng> gpsRange) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < gpsRange.size(); i++) {
            LatLng latLng = gpsRange.get(i);
            if (TrackUtils.checkLatLng(latLng)) {
                throw new SafetyHatException("错误的经纬度");
            }
            stringBuffer.append(latLng.getLatitude()).append(",").append(latLng.getLongitude());
            if (i < gpsRange.size() - 1) {
                stringBuffer.append(";");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 组装请求
     *
     * @return
     */
    public static Map<String, String> packRequest(BaseRequest request) {
        if (null == request) {
            throw new IllegalTrackArgumentException("request can not be null.");
        }
        return TrackUtils.packHttpRequestParams(request);
    }

    /**
     * 请求成功验证
     *
     * @param jsonObject
     * @return
     */
    public static boolean commonResponseVerify(JSONObject jsonObject) {
        /*JSONObject jsonObject = JSONObject.parseObject(result);*/
        return jsonObject.containsKey("status") && jsonObject.getInteger("status") == 0;
    }


    /**
     * 解析响应
     *
     * @param requestId 响应对应的请求ID
     * @param action    响应对应的请求action
     * @param result    响应结果
     */
    public static void parseResponse(long requestId, String action, String result) {
        List<OnUploadListener> listeners = LBSAlarmRangeClient.getInstance().uploadListeners;
        BaseResponse response = new BaseResponse(requestId, StatusCodes.SUCCESS, StatusCodes.MSG_SUCCESS);
        parseCommonResponse(response, result);
        for (OnUploadListener listener : listeners) {
            if (response.getStatus() == StatusCodes.SUCCESS) {
                listener.onSuccess(requestId);
            } else {
                listener.onFailed(response);
            }
        }
    }

    /**
     * 解析通用响应
     *
     * @param response
     * @param result
     */
    private static void parseCommonResponse(BaseResponse response, String result) {
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("status")) {
                response.setStatus(jsonObject.getInteger("status"));
            }
            if (jsonObject.containsKey("message")) {
                response.setMessage(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            response.setStatus(StatusCodes.PARSE_FAILED);
            response.setMessage(StatusCodes.MSG_PARSE_FAILED);
        }
    }

}
