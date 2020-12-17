package com.wyfx.business.dao;

import com.wyfx.business.controller.ws.pojo.OfflineBroadcastVo;
import com.wyfx.business.entity.OfflineBroadcastMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfflineBroadcastMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OfflineBroadcastMessage record);

    int insertSelective(OfflineBroadcastMessage record);

    OfflineBroadcastMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OfflineBroadcastMessage record);

    int updateByPrimaryKey(OfflineBroadcastMessage record);

    int deleteByAccount(String account);

    List<OfflineBroadcastVo> findMessageByAccount(String account);

}
