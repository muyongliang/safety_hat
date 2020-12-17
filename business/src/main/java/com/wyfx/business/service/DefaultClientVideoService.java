package com.wyfx.business.service;

import com.wyfx.business.entity.DefaultClientVideo;

/**
 * 默认视频参数业务处理
 */
public interface DefaultClientVideoService {
    /**
     * 更新视频默认参数业务
     *
     * @param vid
     * @param defaultClientVideo
     */
    void updateDefaultClientVideoSetting(Long vid, DefaultClientVideo defaultClientVideo);
}
