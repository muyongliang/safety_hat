package com.wyfx.business.utils;

import com.wyfx.business.controller.ws.pojo.Account;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.CallFunctionCtrl;
import com.wyfx.business.controller.ws.pojo.DataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author johnson liu
 * @date 2019/12/4
 * @description 定义常量
 */
public class ConstantList {
    private static final Logger logger = LoggerFactory.getLogger(ConstantList.class);
    public static Map<String, Map<String, Session>> sessionMap = new ConcurrentHashMap<>();//保存所有的webSocket连接
    public static List<String> answeringUserList = new CopyOnWriteArrayList<>();//保存所有通话中的用户
    public static Map<String, List<DataBean.MemberBean>> membersByRoom = new ConcurrentHashMap<>();//根据房间号保存通话中的成员
    public static Map<String, List<Account>> targetUserList = new ConcurrentHashMap<>();//通过房间号保存待接听的用户

    public static Map<String, CallFunctionCtrl> functionBySocketId = new ConcurrentHashMap<>();//通过socketId保存通话功能信息


    public synchronized static void removeAnsweringUserList(String userName) {
        answeringUserList.remove(userName);
    }

    /**
     * 移除sessionMap
     *
     * @param userName
     * @param source
     */
    public synchronized static void removeSession(String userName, String source) {
        Map<String, Session> map = sessionMap.get(userName);
        if (map != null) {
            map.remove(source);
            Set keySet = map.keySet();
            if (keySet.size() < 1) {
                sessionMap.remove(userName);
            } else {
                sessionMap.put(userName, map);
            }
        }
    }

    /**
     * 移除房间成员
     *
     * @param roomId
     * @param accountId
     */
    public synchronized static void removeUserByRoom(String roomId, String accountId) {
        List<Account> memberBeanList = targetUserList.get(roomId);
        if (memberBeanList != null) {
            for (Account account : memberBeanList) {
                if (account.getId().equals(accountId)) {
                    memberBeanList.remove(account);
                    break;
                }
            }
            if (memberBeanList.size() < 1) {
                targetUserList.remove(roomId);
            } else {
                targetUserList.put(roomId, memberBeanList);
            }
        }
    }

    /**
     * 移除房间成员
     *
     * @param roomId
     * @param accountId
     */
    public synchronized static void removeMembersByRoom(String roomId, String accountId) {
        List<DataBean.MemberBean> memberBeanList = membersByRoom.get(roomId);
        logger.info("移除房间成员:" + accountId);
        if (memberBeanList != null) {
            for (DataBean.MemberBean memberBean : memberBeanList) {
                if (memberBean.getMemberId().equals(accountId)) {
                    memberBeanList.remove(memberBean);
                    break;
                }
            }
            if (memberBeanList.size() < 1) {
                membersByRoom.remove(roomId);
            } else {
                membersByRoom.put(roomId, memberBeanList);
            }
        }
    }

    public synchronized static void sendMessage(String userName, String message) throws Exception {
        Map<String, Session> map = sessionMap.get(userName);
        Session session = (map == null) ? null : map.get("android");
        //查询websocket中是否有保存连接
        if (session == null) {
            return;
        }
        session.getBasicRemote().sendText(message);
        logger.info("发送了消息:" + message);
    }

    public static BaseCommand buildBaseCommand(Object message, String eventType, String type) throws Exception {
        BaseCommand command = new BaseCommand();
        command.setEventName(eventType);
        command.setType(type);
        command.setData(message);
        return command;
    }


}
