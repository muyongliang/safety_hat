package com.wyfx.total.service;

import com.wyfx.total.entity.PullMessage;

import java.util.List;
import java.util.Map;

/**
 * 官方推送消息接口
 */
public interface IPushMessageService {

    /**
     * 添加官方发布消息
     *
     * @param pullMessage
     * @return
     */
    boolean addOfficialMessages(PullMessage pullMessage);

    /**
     * 标题模糊查询
     *
     * @param title
     * @return
     */
    Map queryPullMessageByTitle(String title, int pageNum, int pageSize);

    /**
     * 查看详细
     *
     * @param pid
     * @return
     */
    PullMessage queryPullMessageById(Long pid);


    boolean deletePullMessageById(Long pid);

    boolean updatePullMessage(PullMessage pullMessage);

    Map queryAllPullMessage(int pageNum, int pageSize);

    /**
     * 查询所有当天需要推送的未推送消息
     *
     * @return
     */
    List queryAllUnPushMessages();

}
