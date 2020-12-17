package com.wyfx.business.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

/**
 * 测试推送消息
 * create by  wsm on 2019-11-1-27
 */
//@Component
//@ClientEndpoint
public class SocketClient {

    // 服务端的IP和端口号(总后台的ip端口) 后期通过@value 注入url
    private static final String URL = "192.168.0.180:8081";

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private Session session;

    @PostConstruct
    void init() {
        try {
            //本机地址 即企业服务器所在主机地址
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("hostAddress=================" + hostAddress);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String wsUrl = "ws://" + URL + "/" + hostAddress;
            URI uri = URI.create(wsUrl);
            session = container.connectToServer(SocketClient.class, uri);
        } catch (DeploymentException | IOException e) {
            System.out.println("异常====================================");
            e.printStackTrace();
        }
    }

    /**
     * 打开连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    /**
     * 接收消息
     *
     * @param text
     */
    @OnMessage
    public void onMessage(String text) {
        System.out.println("接受消息=" + text);
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
     */
    @OnClose
    public void onClosing() throws IOException {
        System.out.println("client  close ");
        session.close();
    }

    /**
     * 主动发送消息
     */
    public void send(JSONObject json) {
        System.out.println("client send");
        session.getAsyncRemote().sendText(json.toJSONString());
    }

}

