package com.wyfx.business.controller.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.controller.ws.pojo.*;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.OfflineBroadcastMessage;
import com.wyfx.business.entity.RecordManager;
import com.wyfx.business.entity.vo.MemberBeanVo;
import com.wyfx.business.entity.vo.MessageVo;
import com.wyfx.business.service.OfflineBroadcastMessageService;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.service.TalkBackGroupMemberService;
import com.wyfx.business.service.TalkBackService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.ConstantList;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/video/{source}/{bid}")
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static List<BaseCommand> baseCommands;
    private static BusinessUserService businessUserService;
    private static TalkBackGroupMemberService talkBackGroupMemberService;
    private static RecordManagerService recordManagerService;
    private static OfflineBroadcastMessageService offlineBroadcastMessageService;
    private static TalkBackService talkBackService;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据 control
    private Session session;
    private String sid;
    private String source;
    private BusinessUser businessUser;

    /**
     * 给所有(android)用户发送消息
     *
     * @param message
     */
    public static void sendAllMessage(String message) {
        for (WebSocketServer item : webSocketSet) {
            try {
                if ("web".equals(item.source)) {
                    continue;
                }
                item.sendMessage(message, item.sid);

            } catch (IOException e) {
                logger.error("发送消息失败", e);
                continue;
            } catch (Exception e) {
                logger.error("发送消息失败", e);
                continue;
            }
        }
    }

    /**
     * 发送给指定用户类型消息
     *
     * @param message
     * @param userType
     */
    public static void sendAllMessage(String message, Integer userType, String toWay, String token) {


        sendAllMessage(message, userType, null, toWay, token);
    }

    /**
     * 群发消息
     *
     * @param message   消息内容
     * @param userType  用户类型
     * @param projectId 项目ID
     * @param toWay     指定发送给web端还是android
     */
    public static void sendAllMessage(String message, Integer userType, Long projectId, String toWay, String token) {
        if (webSocketSet.isEmpty()) {
            return;
        }
        for (WebSocketServer item : webSocketSet) {
            try {
                if (toWay != null && !toWay.equals(item.source)) {
                    continue;
                }
                if (userType != null && !item.businessUser.getUserType().equals(userType)) {
                    continue;
                }
                if (projectId != null && !item.businessUser.getProjectId().equals(projectId)) {
                    continue;
                }
                if (token != null && !token.equals(item.businessUser.getToken())) {
                    continue;
                }
                item.sendMessage(message, item.sid);
            } catch (IOException e) {
                logger.error("发送消息失败", e);

                continue;
            } catch (Exception e) {
                logger.error("发送消息失败", e);
                continue;
            }
        }
    }

    public static void sendAllMessage(String sid, String message, Integer userType, Long projectId, String toWay, String token) {
        logger.info("等待发送消息:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                if (toWay != null && !toWay.equals(item.source)) {
                    continue;
                }
                if (userType != null && !item.businessUser.getUserType().equals(userType)) {
                    continue;
                }
                if (projectId != null && !item.businessUser.getProjectId().equals(projectId)) {
                    continue;
                }
                if (token != null && !token.equals(item.businessUser.getToken())) {
                    continue;
                }
                if (item.sid.equals(sid)) {
                    item.sendMessage(message, item.sid);
                }
            } catch (IOException e) {
                logger.error("发送消息失败", e);
                continue;
            } catch (Exception e) {
                logger.error("发送消息失败", e);
                continue;
            }
        }
    }

    @Autowired
    public void setBusinessUserService(BusinessUserService businessUserService) {
        WebSocketServer.businessUserService = businessUserService;
    }

    @Autowired
    public void setTalkBackGroupMemberService(TalkBackGroupMemberService talkBackGroupMemberService) {
        WebSocketServer.talkBackGroupMemberService = talkBackGroupMemberService;
    }

    @Autowired
    public void setRecordManagerService(RecordManagerService recordManagerService) {
        WebSocketServer.recordManagerService = recordManagerService;
    }

    @Autowired
    public void setOfflineBroadcastMessageService(OfflineBroadcastMessageService offlineBroadcastMessageService) {
        WebSocketServer.offlineBroadcastMessageService = offlineBroadcastMessageService;
    }

    @Autowired
    public void setTalkBackService(TalkBackService talkBackService) {
        WebSocketServer.talkBackService = talkBackService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("bid") String sid, @PathParam("source") String source) {

        try {
            if (sid == null) {
                session.close();
                logger.debug(sid);
                return;
            }
            businessUser = businessUserService.findByUserName(sid);
            if (businessUser == null) {
                logger.debug(sid + ":用户未登录!");
                session.close();
                return;
            }
            System.out.println("用户[" + sid + "]建立了连接,SessionId:" + session.getId() + "\n" + businessUser);
            Map<String, Session> map = ConstantList.sessionMap.get(sid);


            if (map != null && map.get(source) != null) {
                if (map.get(source) != session) {
                    try {
                        map.get(source).close();//将之前的连接强制关闭
                    } catch (Exception e) {
                        logger.debug("关闭上一个连接异常！" + e.toString());
                    }
                }
            }
            map = (map == null) ? new HashMap<>() : map;
            map.put(source, session);//将当前session存入

            this.session = session;
            this.sid = sid;
            this.source = source;

            webSocketSet.add(this);     //加入set中
            ConstantList.sessionMap.put(sid, map);
            // 修改用户在线状态
            int onlineStatus = 0;
            int size = map.keySet().size();
            if (size == 1) {
                onlineStatus = (source.equals(UserTypeAndStatus.sourceOfAndroid)) ? UserTypeAndStatus.MOBILE_ONLINE : UserTypeAndStatus.WEB_ONLINE;
            }
            if (size == 2) {
                onlineStatus = UserTypeAndStatus.WEB_MOBILE_ONLINE;
            }

            logger.debug("websocket中查询到用户:" + sid);
            businessUserService.updateOnlineStatus(sid, onlineStatus);

            if (businessUser.getUserType() == 2) {
                List<OfflineBroadcastVo> offlineBroadcastMessages = offlineBroadcastMessageService.findMessageByAccount(sid);
                logger.debug(sid + ":检测离线广播消息:" + offlineBroadcastMessages.size());
                if (null != offlineBroadcastMessages && offlineBroadcastMessages.size() > 0) {
                    BaseCommand sendCmd = new BaseCommand();
                    sendCmd.setEventName(WsConstant.broadcast.name());
                    sendCmd.setType("offline");
                    sendCmd.setData(offlineBroadcastMessages);
                    session.getBasicRemote().sendText(JSONObject.toJSONString(sendCmd));//发送离线广播消息
                    offlineBroadcastMessageService.deleteMessageByAccount(sid);
                }
            }
            String message = JSON.toJSONString(new BaseCommand(WsConstant.updateOnline.name(), "", ""));
            WebSocketServer.sendAllMessage(message, null, businessUser.getProjectId(), null, null);
        } catch (Exception e) {
            logger.error("建立webSocket连接异常:" + sid, e);
            //检测是否存入数组中
            Map<String, Session> map = ConstantList.sessionMap.get(sid);
            if (map != null && map.get(source) != null) {
                if (session != null && map.get(source) == session) {
                    //移除掉已经存在的Session
                    ConstantList.sessionMap.put(sid, new HashMap<>());
                }
                if (session != null && map.get(source) != session) {
                    try {
                        map.get(source).close();//将之前的连接强制关闭
                    } catch (Exception e1) {
                        logger.debug("关闭上一个连接异常！" + e1.toString());
                    }
                }

            }
        }
    }

    /**
     * 收到消息触发事件
     *
     * @param message
     * @return
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("source") String source) {
        logger.debug("收到消息:" + message);
        JSONObject jsonObject = JSON.parseObject(message);
        BaseCommand command = jsonObject.toJavaObject(BaseCommand.class);

        handler(command);
    }

    @OnError
    public void onError(Session session, Throwable t, @PathParam("source") String source) {
        logger.error(this.sid + ":>>>>连接异常", t);
        //fixme 连接都出现异常了，是否还需要关闭
        //onClose(session, null, source);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason, @PathParam("source") String source) {
        close(session, source);
        logger.error("连接关闭:>>>>" + this.sid, reason);
        String message = JSON.toJSONString(new BaseCommand(WsConstant.updateOnline.name(), "", ""));
        Long projectId = businessUser == null ? null : businessUser.getProjectId();
        WebSocketServer.sendAllMessage(message, null, projectId, null, null);

    }

    public void close(Session session, String source) {
        try {
            webSocketSet.remove(this);  //从set中删除
            logger.error("webSocket连接关闭(sessionId)：" + session + ";用户:" + sid);
            session.close();//关闭webSocket连接
            if (sid == null) {
                return;
            }
            ConstantList.removeSession(sid, source);//移除保存的当前异常连接
            ConstantList.removeAnsweringUserList(sid);//连接断开时主动移除
            // 修改用户的离线状态
            int onlineStatus = 0;
            int size = (ConstantList.sessionMap.get(sid) == null) ? 0 : ConstantList.sessionMap.get(sid).keySet().size();
            if (size == 1) {
                onlineStatus = (source.equals(UserTypeAndStatus.sourceOfAndroid)) ? UserTypeAndStatus.WEB_ONLINE : UserTypeAndStatus.WEB_MOBILE_ONLINE;
            }
            if (size == 0) {
                onlineStatus = UserTypeAndStatus.OFF_LINE;
            }
            businessUserService.updateOnlineStatus(sid, onlineStatus);
        } catch (Exception e) {
            logger.error("webSocket关闭异常", e);
        }
    }

    /**
     * 实现服务器主动推送  todo
     */
    public synchronized void sendMessage(Object message, String sid) throws Exception {
        if (message instanceof String) {
            try {
                BaseCommand baseCommand = JSON.parseObject((String) message, BaseCommand.class);
                baseCommand.setSessionId(sid);
                String s = JSONObject.toJSONString(baseCommand);
                this.session.getBasicRemote().sendText(s);
                logger.debug("发送消息成功：" + s);
                baseCommands = new ArrayList<>();
                if (baseCommand.getEventName().equals("heartCheck")) {
                    logger.debug("发送消息成功：" + sid + " >>>>SessionId:" + session.getId());
                    baseCommands.add(baseCommand);
                }
                logger.error("--------" + baseCommands);
            } catch (Exception e) {
                e.printStackTrace();
                onClose(session, null, "android");
            }
            return;
        }
        logger.debug("发送消息成功：" + sid + " >>>>SessionId:" + session.getId());
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            /*BaseCommand baseCommand= JSONObject.parseObject(message.toString()).toJavaObject(BaseCommand.class);
            if(WsConstant.heartCheck.name().equals(ba9999999999+seCommand.getEventName())){
                //关闭连接
                close(this.session,this.source);
                return;
            }*/
            throw e;
        }
    }

    @Scheduled(fixedRate = 30 * 1000)
    private void heartCheck() {
        logger.info("30s定时检查心跳更改用户状态");
        logger.info("+++++++++{}", baseCommands);
        if (baseCommands == null) {
            return;
        }
        for (BaseCommand baseCommand : baseCommands) {
            if (baseCommand.getTime() < System.currentTimeMillis() - 3000) {
                if (ConstantList.sessionMap.isEmpty()) {
                    return;
                }
                Session session = ConstantList.sessionMap.get(baseCommand.getSessionId()).get("android");
                onClose(session, null, "android");

            }
        }
    }

    /**
     * 自定义消息消息发送
     */
    public void sendInfo(BaseCommand message, String sid) throws IOException {
        sendAllTypeInfo(null, message, sid, null);
    }

    public void sendInfo(WsConstant wsConstant, String message, String sid, String toWay) throws IOException {
        sendAllTypeInfo(wsConstant, message, sid, toWay);
    }

    public void sendAllTypeInfo(WsConstant wsConstant, Object message, String sid, String toWay) {
        if (ConstantList.sessionMap.get(sid) == null) {
            logger.debug("设备【" + sid + "】不在线");
            if (wsConstant != null && wsConstant.equals(WsConstant.broadcast)) {
                //保存离线广播消息
                OfflineBroadcastMessage offlineBroadcastMessage = new OfflineBroadcastMessage();
                String msg = message.toString();
                BaseCommand sendCmd = JSON.parseObject(msg, BaseCommand.class);
                int type = sendCmd.getType().equals("voice") ? 1 : 2;
                String offlineMessage = sendCmd.getData().toString();
                offlineBroadcastMessage.setMessage(offlineMessage);
                offlineBroadcastMessage.setTargetAccount(sid);
                offlineBroadcastMessage.setType(type);
                offlineBroadcastMessageService.saveMessage(offlineBroadcastMessage);
            }
            return;
        }
        sendAllMessage(sid, message.toString(), null, null, toWay, null);
    }

    public void handler(BaseCommand message) {
        logger.error(message.toString());
        try {
            JSONObject data = null;
            String type = message.getType();
            String eventName = message.getEventName();
            String resTime = null;
            //将字符串转为枚举
            WsConstant wsConstant = WsConstant.valueOf(eventName);

            Account resAccount = null;
            DataBean.RoomBean resRoomBean = null;
            List<Account> resTargetBeanList = null;
            if (wsConstant.name().equals("heartCheck")) {
                //不对心跳消息做处理
                List<BaseCommand> list_remove = new ArrayList<>();
                for (BaseCommand baseCommand : baseCommands) {
                    if (message.getSessionId().equals(baseCommand.getSessionId())) {
                        list_remove.add(baseCommand);
                    }
                }
                baseCommands.removeAll(list_remove);
                return;
            }
            switch (wsConstant) {
                case bindingSocketId:
                    bindingSocketId(message);
                    break;
                case findUserInfoBySocketId:
                    findUserInfoBySocketId(message, wsConstant);
                    break;
                case operation:
                    operationHandler(message, wsConstant);
                    break;
                case talkback:
                case broadcast:
                    //对讲/广播
                    data = (JSONObject) JSON.toJSON(message.getData());
                    resTargetBeanList = new ArrayList<>();
                    break;
                default:
                    data = (JSONObject) JSON.toJSON(message.getData());
                    resAccount = data.getObject("my", Account.class);
                    resRoomBean = data.getObject("room", DataBean.RoomBean.class);
                    resTargetBeanList = data.getJSONArray("target").toJavaList(Account.class);
                    resTime = data.getString("createTime");
                    break;
            }
            if (WsConstant.bindingSocketId.name().equals(eventName) || WsConstant.findUserInfoBySocketId.name().equals(eventName) || WsConstant.operation.name().equals(eventName)) {
                return;
            }
            //对语音/视频/对讲/广播进行消息处理
            switch (wsConstant) {
                case invited:
                case invitedVoice:
                case invitedMore:
                case invitedVoiceMore:
                    invitedHandler(message, data, type, wsConstant, resAccount, resRoomBean, resTargetBeanList);
                    break;
                case beInvited:
                case beInvitedVoice:
                case beInvitedMore:
                case beInvitedVoiceMore:
                    beInvitedHandler(message, data, resTime, wsConstant, resAccount, resRoomBean, resTargetBeanList);
                    break;
                case invitedClose:
                case invitedVoiceClose:
                case invitedMoreClose:
                case invitedVoiceMoreClose:
                    invitedCloseHandler(wsConstant, type, resTime, resAccount, resRoomBean, resTargetBeanList);
                    break;
                case voiceConversationEnd:
                case voiceMoreConversationEnd:
                case videoConversationEnd:
                case videoMoreConversationEnd:
                    endCallHandler(wsConstant, message, resTime, resAccount, resRoomBean, resTargetBeanList);
                    break;
                case talkback:
                    //对讲
                    talkBackHandler(wsConstant, message.getType(), data);
                    break;
                case broadcast:
                    //广播
                    broadcastHandler(message, data, wsConstant);
                    break;
                case beInvitedVoiceBusy:
                case beInvitedBusy:
                    beInvitedBusyHandler(message, wsConstant, resTargetBeanList);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("处理消息失败", e);
        }
    }

    /**
     * 排队事件处理
     *
     * @param message
     */
    private void beInvitedBusyHandler(BaseCommand message, WsConstant wsConstant, List<Account> resTargetBeanList) throws Exception {
        DataBean dataBean = JSONObject.toJavaObject((JSONObject) message.getData(), DataBean.class);
        logger.info("呼叫排队:" + dataBean);
        BaseCommand sendCmd = new BaseCommand(getResEventName(wsConstant), message.getType(), dataBean);
        sendInfoFromTargetBeanList(wsConstant, resTargetBeanList, sendCmd, null);
    }

    /**
     * 被邀请事件处理
     *
     * @param message
     * @param data
     * @param resTime
     * @param wsConstant
     * @param resAccount
     * @param resRoomBean
     * @param resTargetBeanList
     * @return
     * @throws IOException
     */
    private void beInvitedHandler(BaseCommand message, JSONObject data, String resTime, WsConstant wsConstant, Account resAccount, DataBean.RoomBean resRoomBean, List<Account> resTargetBeanList) throws IOException {
        String type = message.getType();
        String toWay = null;
        //判断是否已经被接听,如果是终端向调度员发起，则返回xxx已经接听,否则让其加入等待队列
        //如果未被接听,则直接向邀请方回复同意与否
        List<DataBean.MemberBean> memberBeanList = ConstantList.membersByRoom.get(resRoomBean.getRoomId());
        List<Account> targetList = new ArrayList<>();
        boolean isTalking = false;
        DataBean dataBean = new DataBean();
        if (memberBeanList != null) {
            for (int i = 0; i < memberBeanList.size(); i++) {
                if (resTargetBeanList != null) {
                    for (int j = 0; j < resTargetBeanList.size(); j++) {
                        Account account = resTargetBeanList.get(j);
                        if (resTargetBeanList.get(j).getId().equals(memberBeanList.get(i).getMemberId())) {
                            if (WsConstant.z2d.name().equals(type) && memberBeanList.size() > 1) {
                                //向所有target用户返回"拒绝连接，xxxx已处理该连接"
                                targetList.add(resAccount);
                                dataBean.setTarget(targetList);
                                account.setState("0");
                                dataBean.setMy(account);
                                dataBean.setDes("拒绝连接，" + resAccount.getName() + "已接听该通话");
                                dataBean.setRoom(resRoomBean);
                                dataBean.setMember(new ArrayList<>());
                                dataBean.setCreateTime(data.getString("createTime"));
                                isTalking = true;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        boolean flag = true;
        if (!isTalking) {
            //根据状态判断被邀请方是否同意接听
            if ("1".equals(resAccount.getState())) {
                //PC跟移动端同时在线时,判断是否已经被接听，如果当前接听
                /*boolean flag=true;*/
                if (ConstantList.answeringUserList.contains(sid)) {
                    sendBeInvitedCloseCmd(true, message, resTime, wsConstant, resAccount);
                    flag = false;
                }
                if (WsConstant.z2d.name().equals(type)) {
                    //当其中一个调度员接听后，通知其他调度员主动挂断
                    List<BusinessUser> dispatcherList = businessUserService.findDispatcherByAccount(resAccount.getId());
                    List<Account> accountList = new ArrayList<>();

                    BaseCommand closeSendCmd = new BaseCommand();
                    closeSendCmd.setEventName(getResCloseCmd(wsConstant));
                    closeSendCmd.setType(message.getType());

                    for (BusinessUser user : dispatcherList) {
                        //给其他在线的调度员发送挂断命令
                        accountList.clear();
                        if (user.getOnlineStatus() == 0 || this.sid.equals(user.getUserName())) {
                            continue;
                        }
                        accountList.add(new Account("z", null, user.getUserName(), user.getName(), "0"));
                        dataBean.setMy(resAccount);
                        dataBean.setCreateTime(resTime);
                        dataBean.setDes("通话已被其他调度员接听");
                        dataBean.setMember(new ArrayList<DataBean.MemberBean>());
                        dataBean.setRoom(new DataBean.RoomBean());
                        dataBean.setTarget(accountList);
                        closeSendCmd.setData(dataBean);
                        sendAllTypeInfo(wsConstant, JSONObject.toJSONString(closeSendCmd), user.getUserName(), null);
                    }
                }
                if (flag) {
                    sendBeInvitedCloseCmd(false, message, resTime, wsConstant, resAccount);

                    //同意后，将该账号加入member中，同时保存到通话列表中,如果后续有通话进来，将进行排队处理
                    DataBean.MemberBean memberBean = new DataBean.MemberBean();
                    memberBean.setMemberId(resAccount.getId());
                    memberBean.setMemberName(resAccount.getName());
                    memberBean.setAccountType(resAccount.getAccountType());
                    memberBean.setHeadUrl(resAccount.getHeadUrl());
                    memberBean.setState("1");

                    synchronized (ConstantList.membersByRoom) {
                        if (memberBeanList != null) {
                            memberBeanList.add(memberBean);
                        }
                        ConstantList.membersByRoom.put(resRoomBean.getRoomId(), memberBeanList);
                    }

                    ConstantList.answeringUserList.add(resAccount.getId());
                    dataBean.setMy(resAccount);
                    dataBean.setRoom(resRoomBean);
                    /*dataBean.setTarget(ConstantList.targetUserList.get(resRoomBean.getRoomId()));*/
                    dataBean.setTarget(resTargetBeanList);
                    dataBean.setMember(ConstantList.membersByRoom.get(resRoomBean.getRoomId()));
                    dataBean.setDes("同意建立连接");
                    dataBean.setCreateTime(data.getString("createTime"));
                }
            }
            if ("0".equals(resAccount.getState())) {
                //拒绝通话
                ConstantList.removeUserByRoom(resRoomBean.getRoomId(), resAccount.getId());
                resAccount.setState("0");
                System.out.println("resAccount:" + resAccount);
                dataBean.setMy(resAccount);
                dataBean.setRoom(resRoomBean);
                /*List<Account> list=ConstantList.targetUserList.get(resRoomBean.getRoomId());*/
                List<Account> list = resTargetBeanList;
                list = (list == null) ? new ArrayList<>() : list;
                if (WsConstant.z2d.name().equals(type)) {
                    List<Account> targetUserList = ConstantList.targetUserList.get(resRoomBean.getRoomId());
                    targetUserList = (targetUserList == null) ? new ArrayList<>() : targetUserList;
                    dataBean.setTarget(targetUserList);
                } else {
                    dataBean.setTarget(list);
                }
                dataBean.setMember(new ArrayList<>());
                dataBean.setDes("通话已被拒绝");
                dataBean.setCreateTime(data.getString("createTime"));

            }
            /*if("2".equals(resAccount.getState())){
                //等待排队
            }*/
        }
        if (!flag) {
            return;
        }
        //发送回复消息
        BaseCommand sendCmd = new BaseCommand(getResEventName(wsConstant), type, dataBean);

        String str = null;
        boolean eventFlag = false;
        String beInvitedResEvent = null;
        BaseCommand sendCmdOfBeInvited = new BaseCommand();
        toWay = ("0".equals(resAccount.getState())) ? null : source;
        for (int i = 0; resTargetBeanList != null && i < resTargetBeanList.size(); i++) {
            logger.debug("循环发送消息到resTargetBeanList>>>");

            //服务器向被邀请人回复消息
            switch (wsConstant) {
                case beInvited:
                    beInvitedResEvent = WsConstant.beInvitedRes.name();
                    eventFlag = true;
                case beInvitedVoice:
                    beInvitedResEvent = (eventFlag == false) ? WsConstant.beInvitedVoiceRes.name() : beInvitedResEvent;
                    eventFlag = true;
                case beInvitedMore:
                    beInvitedResEvent = (eventFlag == false) ? WsConstant.beInvitedMoreRes.name() : beInvitedResEvent;
                    eventFlag = true;
                case beInvitedVoiceMore:
                    beInvitedResEvent = (eventFlag == false) ? WsConstant.beInvitedVoiceMoreRes.name() : beInvitedResEvent;
                    DataBean beInvitedOfDataBean = new DataBean();
                    BeanUtils.copyProperties(dataBean, beInvitedOfDataBean);

                    sendCmdOfBeInvited.setEventName(beInvitedResEvent);
                    sendCmdOfBeInvited.setType(message.getType());
                    Account account = resTargetBeanList.get(i);
                    if ("0".equals(resAccount.getState())) {
                        account.setState("0");
                    }
                    beInvitedOfDataBean.setMy(account);
                    List<Account> accountList = new ArrayList<>();
                    accountList.add(resAccount);
                    beInvitedOfDataBean.setTarget(accountList);
                    sendCmdOfBeInvited.setData(beInvitedOfDataBean);
                    sendInfo(wsConstant, JSONObject.toJSONString(sendCmdOfBeInvited), resAccount.getId(), toWay);
                    break;
                case voiceConversationEnd:
                case voiceMoreConversationEnd:
                case videoConversationEnd:
                case videoMoreConversationEnd:
                    ConstantList.removeAnsweringUserList(resTargetBeanList.get(i).getId());
                    break;
                default:
                    break;
            }
            sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), resTargetBeanList.get(i).getId(), null);
        }
    }

    /**
     * 未接听,邀请方主动挂断事件处理
     *
     * @param wsConstant
     * @param type
     * @param resTime
     * @param resAccount
     * @param resRoomBean
     * @param resTargetBeanList
     * @throws Exception
     */
    private void invitedCloseHandler(WsConstant wsConstant, String type, String resTime, Account resAccount, DataBean.RoomBean resRoomBean, List<Account> resTargetBeanList) throws Exception {
        DataBean dataBean = new DataBean();
        //未接听,邀请方主动挂断
        if (WsConstant.z2d.name().equals(type)) {
            List<BusinessUser> list = businessUserService.findDispatcherByAccount(resAccount.getId());
            resTargetBeanList.clear();
            for (BusinessUser user : list) {
                Account account = new Account();
                account.setAccountType("d");
                account.setId(user.getUserName());
                account.setName(user.getName());
                account.setState("0");
                account.setHeadUrl("");
                resTargetBeanList.add(account);
            }
            dataBean.setRoom(new DataBean.RoomBean());
        } else {
            dataBean.setRoom(resRoomBean);
        }

        ConstantList.removeUserByRoom(resRoomBean.getRoomId(), resAccount.getId());
        dataBean.setMy(resAccount);
        dataBean.setCreateTime(resTime);
        dataBean.setDes("主动挂断连接");
        dataBean.setTarget(resTargetBeanList);

        ConstantList.removeAnsweringUserList(resAccount.getId());
        ConstantList.removeMembersByRoom(resRoomBean.getRoomId(), resAccount.getId());
        //发送消息
        BaseCommand sendCmd = new BaseCommand(getResEventName(wsConstant), type, dataBean);
        sendInfoFromTargetBeanList(wsConstant, resTargetBeanList, sendCmd, null);
    }

    /**
     * 邀请事件处理
     *
     * @param message
     * @param data
     * @param type
     * @param wsConstant
     * @param resAccount
     * @param resRoomBean
     * @param resTargetBeanList
     * @return
     * @throws IOException
     */
    private void invitedHandler(BaseCommand message, JSONObject data, String type, WsConstant wsConstant, Account resAccount, DataBean.RoomBean resRoomBean, List<Account> resTargetBeanList) throws IOException {
        BaseCommand sendCmd = new BaseCommand();
        DataBean dataBean = new DataBean();
        String toWay = null;
        //发起邀请,服务器生成房间号，并判断被邀请用户是否正在通话中，如果正在通话则存入等待队列中,对于终端向调度员发起的邀请,需服务器查询所有的被邀请方
        //生成房间号,然后将此房间号发送给被邀请人
        if (resRoomBean.getRoomId().equals("-1")) {
            resRoomBean = new DataBean.RoomBean(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()), resRoomBean.getRoomUrl());
        }
        if (WsConstant.z2d.name().equals(type)) {
            //查询调度员列表
            List<BusinessUser> list = businessUserService.findDispatcherByAccount(resAccount.getId());
            resTargetBeanList = new ArrayList<>();
            String key = null;
            for (BusinessUser user : list) {
                key = user.getUserName().trim();
                Account targetBean = new Account();
                if (ConstantList.sessionMap.containsKey(key)) {
                    targetBean.setAccountType("d");
                    targetBean.setId(user.getUserName());
                    targetBean.setName(user.getName());
                    targetBean.setState("0");
                    targetBean.setHeadUrl("");
                    resTargetBeanList.add(targetBean);
                }

            }
            dataBean.setTarget(resTargetBeanList);
        } else {
            Iterator<Account> iterator = resTargetBeanList.iterator();
            Account account = null;
            Account offLineResAccount = null;
            DataBean invitedOffLineResOfDataBean = new DataBean();
            while (iterator.hasNext()) {
                account = iterator.next();
                if (ConstantList.sessionMap.containsKey(account.getId())) {
                    /*if(ConstantList.answeringUserList.contains(account.getId())){
                        //--对于通话中的用户,客户端需将该用户加入等待队列中,服务需发送指定类型的命令
                        account.setState("2");
                    }*/
                } else {
                    //离线中,通知发起人接收方离线
                    offLineResAccount = new Account();
                    BeanUtils.copyProperties(account, offLineResAccount);
                    offLineResAccount.setState("3");
                    sendCmd.setEventName(WsConstant.invitedRes.name());
                    sendCmd.setType(message.getType());

                    invitedOffLineResOfDataBean.setMy(offLineResAccount);
                    invitedOffLineResOfDataBean.setRoom(resRoomBean);
                    invitedOffLineResOfDataBean.setMember(new ArrayList<>());
                    invitedOffLineResOfDataBean.setDes("对方不在线");
                    invitedOffLineResOfDataBean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                    invitedOffLineResOfDataBean.setTarget(new ArrayList<>());
                    List<Account> accountList = new ArrayList<>();
                    accountList.add(resAccount);
                    invitedOffLineResOfDataBean.setTarget(accountList);

                    sendCmd.setData(invitedOffLineResOfDataBean);
                    sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), resAccount.getId(), toWay);
                    iterator.remove();
                }
            }
            dataBean.setTarget((List<Account>) data.get("target"));
        }
        List<DataBean.MemberBean> memberBeanListOfInvited = ConstantList.membersByRoom.get(resRoomBean.getRoomId());
        if (memberBeanListOfInvited == null) {
            memberBeanListOfInvited = new ArrayList<>();
        }
        DataBean.MemberBean memberBeanOfInvited = new DataBean.MemberBean(resAccount.getAccountType(), resAccount.getHeadUrl(), resAccount.getId(), resAccount.getName(), resAccount.getState());
        memberBeanListOfInvited.add(memberBeanOfInvited);
        ConstantList.membersByRoom.put(resRoomBean.getRoomId(), memberBeanListOfInvited);
        dataBean.setMy(resAccount);
        dataBean.setMember(memberBeanListOfInvited);
        dataBean.setRoom(resRoomBean);
        dataBean.setDes("服务器中转邀请");
        dataBean.setCreateTime(data.getString("createTime"));
        ConstantList.targetUserList.put(resRoomBean.getRoomId(), resTargetBeanList);
        //发送消息
        sendCmd.setEventName(getResEventName(wsConstant));
        sendCmd.setType(type);
        sendCmd.setData(dataBean);
        sendInfoFromTargetBeanList(wsConstant, resTargetBeanList, sendCmd, toWay);
    }

    /**
     * 将消息发送给resTargetBeanList中的所有用户
     *
     * @param wsConstant
     * @param resTargetBeanList
     * @param sendCmd
     * @param toWay
     * @throws IOException
     */
    private void sendInfoFromTargetBeanList(WsConstant wsConstant, List<Account> resTargetBeanList, BaseCommand sendCmd, String toWay) throws IOException {
        String targetId = null;
        String str = null;
        for (int i = 0; resTargetBeanList != null && i < resTargetBeanList.size(); i++) {
            Account target = resTargetBeanList.get(i);
            targetId = target.getId();
            str = JSONObject.toJSONString(sendCmd);
            /*str=(beInvitedFlag)?JSONObject.toJSONString(sendCmdOfBeInvited):JSONObject.toJSONString(sendCmd);*/
            System.out.println("发送信息:\n" + str);
            /*if(toWay!=null && targetId.equals(sid)){
                continue;
            }*/
            sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), targetId, toWay);
        }
    }

    /**
     * 挂断已接听通话
     *
     * @param wsConstant
     * @param resTime
     * @param resAccount
     * @param resRoomBean
     * @param resTargetBeanList
     */
    private void endCallHandler(WsConstant wsConstant, BaseCommand message, String resTime, Account resAccount, DataBean.RoomBean resRoomBean, List<Account> resTargetBeanList) throws Exception {
        //已接听,用户挂断,结束会话事件
        //如果是调度员主动挂断,则将该房间中所有的通话挂断
        //将用户从接听状态移除
        Object data = message.getData();
        logger.error(data.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        MessageVo messageVo = objectMapper.convertValue(data, MessageVo.class);
        if (messageVo.getTarget().size() == 1) {
            for (MemberBeanVo memberBeanVo : messageVo.getTarget()) {
                logger.error("是否移除" + resRoomBean.getRoomId() + "-------------" + resAccount.getId());
                ConstantList.removeAnsweringUserList(memberBeanVo.getId());
                ConstantList.removeMembersByRoom(resRoomBean.getRoomId(), memberBeanVo.getId());
                //会话发起人结束通话时，关闭所有通话
                DataBean dataBean = new DataBean();
                dataBean.setDes(memberBeanVo.getId().substring(memberBeanVo.getId().lastIndexOf("_") + 1) + "结束会话");
                dataBean.setRoom(resRoomBean);
                dataBean.setCreateTime(resTime);
                dataBean.setMy(resAccount);

                BaseCommand sendCmd = new BaseCommand();
                sendCmd.setEventName(getResEventName(wsConstant));
                sendCmd.setType(message.getType());
                sendCmd.setData(dataBean);
                sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), memberBeanVo.getId(), null);
            }
            return;
        }

        List<DataBean.MemberBean> endMemberList = ConstantList.membersByRoom.get(resRoomBean.getRoomId());

        /*List<Account> endTargetList=new ArrayList<>();*/
        resTargetBeanList.clear();
        if (endMemberList != null && endMemberList.get(0).getMemberId().equals(resAccount.getId())) {
            //会话发起人结束通话时，关闭所有通话
            DataBean dataBean = new DataBean();
            dataBean.setDes(resAccount.getName() + "结束会话");
            dataBean.setRoom(resRoomBean);
            dataBean.setCreateTime(resTime);
            dataBean.setMy(resAccount);

            BaseCommand sendCmd = new BaseCommand();
            sendCmd.setEventName(getResEventName(wsConstant));
            sendCmd.setType(message.getType());
            sendCmd.setData(dataBean);
            for (DataBean.MemberBean memberBean : endMemberList) {
                if (memberBean.getMemberId().equals(resAccount.getId())) {
                    continue;
                }
                Account account = new Account(memberBean.getAccountType(), memberBean.getHeadUrl(), memberBean.getMemberId(), memberBean.getMemberName(), "0");
                resTargetBeanList.add(account);
                ConstantList.removeAnsweringUserList(account.getId());
                sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), account.getId(), null);
            }
            dataBean.setMember(new ArrayList<DataBean.MemberBean>());
            dataBean.setTarget(resTargetBeanList);
            ConstantList.membersByRoom.remove(resRoomBean.getRoomId());
        } else {
            //将用户从房间中移除
            ConstantList.removeMembersByRoom(resRoomBean.getRoomId(), resAccount.getId());
        }
    }

    /**
     * 对讲消息处理
     *
     * @param wsConstant
     * @param type
     * @param data
     */
    private void talkBackHandler(WsConstant wsConstant, String type, JSONObject data) throws Exception {
        BusinessUser findUser = null;
        BaseCommand sendCmd = new BaseCommand();
        /*WsConstant wsConstant=WsConstant.valueOf(message.getEventName());*/
        TalkbackCmd talkbackCmd = JSON.toJavaObject(data, TalkbackCmd.class);
        sendCmd.setData(talkbackCmd);
        sendCmd.setEventName(getResEventName(wsConstant));
        sendCmd.setType(type);

        List<ContactsVo> contactsVoList = talkBackGroupMemberService.findByGroupId(Integer.valueOf(talkbackCmd.getGroupId()));
        StringBuffer t_sendToName = new StringBuffer();
        StringBuffer t_sendToBid = new StringBuffer();
        if (contactsVoList != null) {
            Map onlineMap = ConstantList.sessionMap.get(this.sid);
            for (ContactsVo contactsVo : contactsVoList) {
                if (onlineMap != null && !contactsVo.getUserName().equals(this.sid)) {
                    //只给在线设备发送对讲消息
                    Account account = new Account(null, null, contactsVo.getUserName(), null, null);
                    /*resTargetBeanList.add(account);*/
                    findUser = businessUserService.findByUserName(account.getId());
                    t_sendToName.append(findUser.getUserName());
                    t_sendToName.append("/");
                    t_sendToBid.append(findUser.getBid() + ",");

                    sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), account.getId(), null);
                }
            }
        }
        if (talkBackService.findByGroupId(Long.valueOf(talkbackCmd.getGroupId())).getIsRecording() == 1) {
            // 添加对讲记录
            findUser = businessUserService.findByUserName(this.sid);

            RecordManager recordManagerOfTalkBack = new RecordManager(null, new Date(), findUser.getProjectId(), findUser.getBid(), findUser.getName(), t_sendToBid.toString(), t_sendToName.toString(), talkbackCmd.getUrl(), 2, Long.valueOf(talkbackCmd.getGroupId()), 3, null, null, null);
            recordManagerService.addRecord(recordManagerOfTalkBack);
        }
    }

    /**
     * 广播消息处理
     *
     * @param message    消息内容
     * @param data
     * @param wsConstant
     * @return
     * @throws IOException
     */
    private void broadcastHandler(BaseCommand message, JSONObject data, WsConstant wsConstant) throws IOException {
        BaseCommand sendCmd = null;
        BusinessUser findUser;
        BroadCastCmd broadCastCmd = JSON.toJavaObject(data, BroadCastCmd.class);
        List<BroadCastCmd.TargetBean> targetBeanList = broadCastCmd.getTarget();
        StringBuffer b_sendToName = new StringBuffer();
        StringBuffer b_sendToBid = new StringBuffer();
        for (BroadCastCmd.TargetBean targetBean : targetBeanList) {
            sendCmd = new BaseCommand();
            sendCmd.setEventName(message.getEventName());
            sendCmd.setType(message.getType());
            sendCmd.setData(broadCastCmd.getContent());
            //转发广播命令
            sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), targetBean.getUserName(), null);
            b_sendToName.append(targetBean.getUserName());
            b_sendToName.append("/");
            findUser = businessUserService.findByUserName(targetBean.getUserName());
            b_sendToBid.append(findUser.getBid() + ",");
        }
        // 添加广播记录
        findUser = businessUserService.findByUserName(this.sid);
        Integer messageType = (message.getType().equals("voice")) ? 2 : 1;
        RecordManager recordManagerOfBroadcast = new RecordManager(null, new Date(), findUser.getProjectId(), findUser.getBid(), findUser.getName(), b_sendToBid.toString(), b_sendToName.toString(), broadCastCmd.getContent(), messageType, null, 1, null, null, null);
        recordManagerService.addRecord(recordManagerOfBroadcast);
    }

    /**
     * 服务器响应客户端事件名称
     *
     * @param wsConstant
     * @return
     */
    private String getResEventName(WsConstant wsConstant) {
        String resEventName = null;
        switch (wsConstant) {
            case invited:
                resEventName = WsConstant.beInvited.name();
                break;
            case invitedVoice:
                resEventName = WsConstant.beInvitedVoice.name();
                break;
            case invitedMore:
                resEventName = WsConstant.beInvitedMore.name();
                break;
            case invitedVoiceMore:
                resEventName = WsConstant.beInvitedVoiceMore.name();
                break;
            case beInvited:
                resEventName = WsConstant.invitedRes.name();
                break;
            case beInvitedBusy:
            case beInvitedVoice:
            case beInvitedVoiceBusy:
                resEventName = WsConstant.invitedVoiceRes.name();
                break;
            case invitedClose:
                resEventName = WsConstant.invitedClose.name();
                break;
            case invitedVoiceClose:
                resEventName = WsConstant.invitedVoiceClose.name();
                break;
            case beInvitedMore:
                resEventName = WsConstant.invitedMoreRes.name();
                break;
            case beInvitedVoiceMore:
                resEventName = WsConstant.invitedVoiceMoreRes.name();
                break;
            case invitedMoreClose:
                resEventName = WsConstant.invitedMoreClose.name();
                break;
            case invitedVoiceMoreClose:
                resEventName = WsConstant.invitedVoiceMoreClose.name();
                break;
            case voiceConversationEnd:
                resEventName = WsConstant.voiceConversationEnd.name();
                break;
            case voiceMoreConversationEnd:
                resEventName = WsConstant.voiceMoreConversationEnd.name();
                break;
            case videoConversationEnd:
                resEventName = WsConstant.videoConversationEnd.name();
                break;
            case videoMoreConversationEnd:
                resEventName = WsConstant.videoMoreConversationEnd.name();
                break;
            case talkback:
                resEventName = WsConstant.talkback.name();
                break;
            case broadcast:
                resEventName = WsConstant.broadcast.name();
                break;
        }
        return resEventName;
    }

    /**
     * operation事件消息处理
     *
     * @param message
     * @param wsConstant
     * @throws IOException
     */
    public void operationHandler(BaseCommand message, WsConstant wsConstant) throws IOException {
        BaseCommand sendCmd = new BaseCommand();
        String eventName = message.getEventName();
        JSONObject jsonObject;
        JSONArray jsonArraySend;
        JSONArray jsonArray = (JSONArray) JSON.toJSON(message.getData());
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            CallFunctionCtrl callFunctionCtrl = JSON.toJavaObject(jsonObject, CallFunctionCtrl.class);
            sendCmd.setEventName(eventName);
            sendCmd.setType(message.getType());
            jsonArraySend = new JSONArray();
            jsonArraySend.add(callFunctionCtrl);
            sendCmd.setData(jsonArraySend);
            sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), callFunctionCtrl.getUsername(), null);
        }
    }

    /**
     * 查询用户的SocketId
     *
     * @param message
     * @param wsConstant
     * @throws IOException
     */
    public void findUserInfoBySocketId(BaseCommand message, WsConstant wsConstant) throws IOException {
        BaseCommand sendCmd = new BaseCommand();
        String eventName = message.getEventName();
        JSONArray jsonArray = (JSONArray) JSON.toJSON(message.getData());
        JSONArray jsonArraySend;
        String socketIdByFind = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            socketIdByFind = jsonArray.getJSONObject(i).getString("socketId");
            CallFunctionCtrl callFunctionCtrled = ConstantList.functionBySocketId.get(socketIdByFind);
            if (callFunctionCtrled == null) {
                callFunctionCtrled = new CallFunctionCtrl("0", "0", "0", "0", socketIdByFind, "username", 1);
            }
            sendCmd.setEventName(eventName);
            sendCmd.setType(message.getType());
            jsonArraySend = new JSONArray();
            jsonArraySend.add(callFunctionCtrled);
            sendCmd.setData(jsonArraySend);
            sendInfo(wsConstant, JSONObject.toJSONString(sendCmd), this.sid, null);
        }
    }

    /**
     * 绑定socketId
     *
     * @param message
     */
    private void bindingSocketId(BaseCommand message) {
        JSONArray jsonArray = (JSONArray) JSON.toJSON(message.getData());
        String socketId = null;
        Integer userType = null;
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            socketId = jsonObject.getString("socketId");
            userType = jsonObject.getInteger("userType");
            CallFunctionCtrl callFunctionCtrl = new CallFunctionCtrl("0", "0", "1", jsonObject.getString("realName"), socketId, jsonObject.getString("username"), userType);
            ConstantList.functionBySocketId.put(socketId, callFunctionCtrl);
        }
    }

    /**
     * PC端与移动端同时在线时，一方接听，主动发送挂断命令至另一方
     *
     * @param answering
     * @param message
     * @param resTime
     * @param wsConstant
     * @param resAccount
     * @throws IOException
     */
    private void sendBeInvitedCloseCmd(boolean answering, BaseCommand message, String resTime, WsConstant wsConstant, Account resAccount) throws IOException {
        businessUser = businessUserService.findByUserName(sid);
        if (businessUser.getOnlineStatus() == UserTypeAndStatus.WEB_MOBILE_ONLINE) {
            //如果PC跟移动端同时在线时，一方接听后，另一方将主动挂断
            String fromSource = null;
            if (answering) {
                //正在通话中
                fromSource = ("web".equals(source)) ? "web" : "android";
            } else {
                //还未接听
                fromSource = ("web".equals(source)) ? "android" : "web";
            }

            Session session = (answering) ? this.session : ConstantList.sessionMap.get(sid).get(fromSource);
            List<Account> accountList = new ArrayList<>();
            Account account = new Account();
            BeanUtils.copyProperties(resAccount, account);
            accountList.add(account);

            DataBean closeDataBean = new DataBean();
            closeDataBean.setMy(resAccount);
            closeDataBean.setCreateTime(resTime);
            closeDataBean.setDes("通话已被接听:" + source);
            closeDataBean.setMember(new ArrayList<DataBean.MemberBean>());
            closeDataBean.setRoom(new DataBean.RoomBean());
            closeDataBean.setTarget(accountList);

            BaseCommand closeSendCmd = new BaseCommand();
            closeSendCmd.setEventName(getResCloseCmd(wsConstant));
            closeSendCmd.setType(message.getType());
            closeSendCmd.setData(closeDataBean);
            session.getBasicRemote().sendText(JSONObject.toJSONString(closeSendCmd));
            logger.info("发送消息:" + JSONObject.toJSONString(closeSendCmd));
        }
    }

    private String getResCloseCmd(WsConstant wsConstant) {
        String resCloseCmd = "";
        switch (wsConstant) {
            case beInvited:
                resCloseCmd = WsConstant.invitedClose.name();
                break;
            case beInvitedVoice:
                resCloseCmd = WsConstant.invitedVoiceClose.name();
                break;
            case beInvitedMore:
                resCloseCmd = WsConstant.invitedMoreClose.name();
                break;
            case beInvitedVoiceMore:
                resCloseCmd = WsConstant.invitedVoiceMoreClose.name();
                break;
            default:
                break;
        }
        return resCloseCmd;
    }


}
