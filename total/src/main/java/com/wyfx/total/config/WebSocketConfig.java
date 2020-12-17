package com.wyfx.total.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
//未配置此项会出现404  错误提示如下  //the HTTP response from the server [404] did not permit the HTTP upgrade to WebSocket
//@EnableWebSocket
@Deprecated
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
