package com.wyfx.business.service;

import com.wyfx.business.controller.ws.pojo.OfflineBroadcastVo;
import com.wyfx.business.dao.OfflineBroadcastMessageMapper;
import com.wyfx.business.entity.OfflineBroadcastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OfflineBroadcastMessageServiceImpl implements OfflineBroadcastMessageService {
    @Autowired
    private OfflineBroadcastMessageMapper offlineBroadcastMessageMapper;

    @Override
    public void saveMessage(OfflineBroadcastMessage offlineBroadcastMessage) {
        offlineBroadcastMessageMapper.insert(offlineBroadcastMessage);
    }

    @Override
    public void deleteMessageByAccount(String account) {
        offlineBroadcastMessageMapper.deleteByAccount(account);
    }

    @Override
    public List<OfflineBroadcastVo> findMessageByAccount(String account) {
        return offlineBroadcastMessageMapper.findMessageByAccount(account);
    }
}
