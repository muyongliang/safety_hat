package com.wyfx.business.service;

import com.wyfx.business.controller.ws.pojo.OfflineBroadcastVo;
import com.wyfx.business.entity.OfflineBroadcastMessage;

import java.util.List;

public interface OfflineBroadcastMessageService {
    void saveMessage(OfflineBroadcastMessage offlineBroadcastMessage);

    void deleteMessageByAccount(String account);

    List<OfflineBroadcastVo> findMessageByAccount(String account);
}
