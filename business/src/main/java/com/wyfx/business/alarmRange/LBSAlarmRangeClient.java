package com.wyfx.business.alarmRange;

import com.wyfx.business.alarmRange.api.AddCirclefenceRequest;
import com.wyfx.business.alarmRange.core.AlarmRangeHandler;
import com.wyfx.business.alarmRange.core.AsyncRequestClient;
import com.wyfx.business.alarmRange.model.OnUploadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/29
 * @description Http请求客户端
 */
public class LBSAlarmRangeClient {

    private static LBSAlarmRangeClient instance = new LBSAlarmRangeClient();

    public List<OnUploadListener> uploadListeners = null;

    private LBSAlarmRangeClient() {
    }

    /**
     * 获取轨迹客户端实例
     *
     * @return
     */
    public static LBSAlarmRangeClient getInstance() {
        if (null == instance) {
            synchronized (LBSAlarmRangeClient.class) {
                if (null == instance) {
                    instance = new LBSAlarmRangeClient();
                    instance.init();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化Client
     */
    public void init() {
        AsyncRequestClient.getInstance().init();
        uploadListeners = new ArrayList<OnUploadListener>();
    }

    /**
     * 开启Client
     */
    public void start() {
        AsyncRequestClient.getInstance().start();
    }

    /**
     * 停止Client
     */
    public void stop() {
        AsyncRequestClient.getInstance().stop();
    }

    /**
     * 销毁Client
     */
    public void destory() {
        AsyncRequestClient.getInstance().destroy();
    }

    /**
     * 注册上传监听器，接收上传结果回调
     *
     * @param listener
     */
    public void registerUploadListener(OnUploadListener listener) {
        uploadListeners.add(listener);
    }

    /**
     * 取消已注册的上传监听器
     *
     * @param listener
     */
    public void unRegisterUploadListener(OnUploadListener listener) {
        uploadListeners.remove(listener);
    }

    /**
     * 创建圆形围栏
     *
     * @param request
     */
    public void createCirclefence(AddCirclefenceRequest request) {
        AlarmRangeHandler.createCirclefence(request);
    }

}
