package com.wyfx.total.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试 推送消息到企业后台
 * create by wsm on 2019-11-27
 */
//@Component
//@ServerEndpoint(value = "/{ip}")//企业端传过来的ip地址
@Deprecated
public class WebSocket {


    private static final Map<String, Session> connections = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);

    /**
     * 根据IP发送消息
     *
     * @param ip
     * @param text
     */
    public static void send(String ip, String text) {
        logger.info("web server send to some ip=" + ip);
        try {
            Session session = connections.get(ip);
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历群发消息
     *
     * @param text
     */
    public static void send(String text) {
        logger.info("web server send to all ");
        for (Map.Entry<String, Session> entry : connections.entrySet()) {
            send(entry.getKey(), text);
        }
    }

    /**
     * 打开连接
     *
     * @param session
     * @param ip
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("ip") String ip) {
        System.out.println("connect ip==========================" + ip);
        connections.put(ip, session);
    }

    /**
     * 接收消息
     *
     * @param text
     */
    @OnMessage
    public void onMessage(String text) {
        System.out.println("client say=" + text);
    }

    /**
     * 异常处理
     *
     * @param throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 关闭连接
     *
     * @param ip
     */
    @OnClose
    public void onClosing(@PathParam("ip") String ip) throws IOException {
        System.out.println("web server close");
        connections.remove(ip);
    }
}






