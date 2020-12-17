package com.wyfx.business.alarmRange.model;

/**
 * 上传监听器
 *
 * @author baidu
 */
public interface OnUploadListener {

    /**
     * 上传成功回调
     *
     * @param responseId 响应ID（与请求ID对应）
     */
    void onSuccess(long responseId);

    /**
     * 上传失败回调
     */
    void onFailed(BaseResponse response);

}
