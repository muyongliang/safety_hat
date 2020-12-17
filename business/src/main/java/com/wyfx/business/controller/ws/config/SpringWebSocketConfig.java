package com.wyfx.business.controller.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/*@Configuration*/
public class SpringWebSocketConfig extends ServerEndpointConfig.Configurator {
    /**
     * 修改握手,就是在握手协议建立之前修改其中携带的内容
     *
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        /*Subject subject=null;
        try {
            subject=SecurityUtils.getSubject();
        }catch (Exception e){
            subject=null;
        }
        if(sec.getUserProperties()!=null){
            sec.getUserProperties().put("subject",subject);
        }
        super.modifyHandshake(sec, request, response);*/
        /*HttpSession session=(HttpSession)request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(),session);//将session存入ServerEndpointConfig类中*/
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //这个对象说一下，貌似只有服务器是tomcat的时候才需要配置,具体我没有研究
        return new ServerEndpointExporter();
    }
}
