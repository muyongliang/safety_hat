package com.wyfx.total.service;

import com.wyfx.total.entity.TopMessages;

import java.util.List;
import java.util.Map;

/**
 * 消息中心
 */
public interface ITopMessagesService {
    /**
     * 添加系统消息
     *
     * @param topMessages
     */
    boolean addTopMessages(TopMessages topMessages);

    /**
     * 通过flag查询所有已读或未读消息
     *
     * @param flag 已读0 未读1
     * @return
     */
    Map QueryAllTopMessagesByFlag(Integer flag, Integer pageNum, Integer pageSize);

    List<Long> handleSelectAllUnreadMessages();


    /**
     * 更新已读消息状态标识 flag
     *
     * @param ids
     * @return
     */
    boolean updateWhoIsRead(List<Long> ids);


}
