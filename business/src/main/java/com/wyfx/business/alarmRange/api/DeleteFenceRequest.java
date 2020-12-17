package com.wyfx.business.alarmRange.api;

import com.wyfx.business.alarmRange.model.BaseRequest;

/**
 * @author johnson liu
 * @date 2019/11/30
 * @description 请求删除围栏
 */
public class DeleteFenceRequest extends BaseRequest {

    private String monitored_person;
    private String fence_ids;
    private String sn;

    public DeleteFenceRequest() {
    }

    /**
     * @param ak        服务端AK
     * @param serviceId 轨迹服务ID
     */
    public DeleteFenceRequest(String ak, long serviceId, String monitored_person, String fence_ids, String sn) {
        super(ak, serviceId);
        this.monitored_person = monitored_person;
        this.fence_ids = fence_ids;
        this.sn = sn;
    }

    public String getMonitored_person() {
        return monitored_person;
    }

    public void setMonitored_person(String monitored_person) {
        this.monitored_person = monitored_person;
    }

    public String getFence_ids() {
        return fence_ids;
    }

    public void setFence_ids(String fence_ids) {
        this.fence_ids = fence_ids;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
