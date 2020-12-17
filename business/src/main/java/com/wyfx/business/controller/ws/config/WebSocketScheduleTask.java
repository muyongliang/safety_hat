package com.wyfx.business.controller.ws.config;

import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description WebSocket定时任务:webSocket定时发送消息类
 * @statement: 以<6 0 s的频率发送给websocket连接的对象 ， 以防止反向代理的60s超时限制
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class WebSocketScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketScheduleTask.class);

    //3.添加定时任务,55秒是考虑5秒的延迟,从而保证60s的心跳
    //@Scheduled(cron = "0/55 * * * * ?")
    //或直接指定时间间隔，例如：55秒
    @Scheduled(fixedRate = 10 * 1000)
    private void configureTasks() throws Exception {
        BaseCommand command = new BaseCommand();
        command.setEventName("heartCheck");
        command.setType("-1");
        command.setData("");
        command.setTime((new Date()).getTime());

        String message = JSONObject.toJSONString(command);
        WebSocketServer.sendAllMessage(message, null, null, null);

    }


}
