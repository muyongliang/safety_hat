package com.wyfx.business.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.entity.BusinessMessageCenter;
import com.wyfx.business.service.common.IBusinessMessagesCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * create by wsm on 2019-12-10
 */
@RestController
@RequestMapping("/messagesCenter")
public class BusinessMessagesCenterController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessMessagesCenterController.class);

    @Autowired
    private IBusinessMessagesCenterService iBusinessMessagesCenterService;

    /**
     * 添加总后台推送消息---发布就推送
     *
     * @param json
     * @return
     */
    @PostMapping("/handleAddPushMessage")
    public String handleAddPushMessage(@RequestBody String json) {
        logger.info("添加总后台推送消息---发布就推送" + json);
        Map map = JSON.parseObject(json, Map.class);
        //企业的token  如果为0代表给所有企业的推送消息
        BusinessMessageCenter messageCenter = new BusinessMessageCenter(
                null, (String) map.get("title"), (String) map.get("content"), 0, 0, new Date(), map.get("token").toString());
        boolean b = iBusinessMessagesCenterService.addBusinessMessages(messageCenter);
        if (b) {
            return "true";
        }
        return "false";
    }

    /**
     * 总后台-----处理定时推送官方消息
     *
     * @return
     */
    @PostMapping("/handleAddScheduledPush")
    public String handleAddScheduledPush(@RequestBody String json) {
        logger.info("总后台-----处理定时推送官方消息 ");
        //json转对象数组
        JSONArray jsonArray = JSONArray.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            //获取数组中每一个对象
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //将对象转为map
            Map map = JSON.parseObject(String.valueOf(jsonObject), Map.class);
            BusinessMessageCenter messageCenter = new BusinessMessageCenter(
                    null, (String) map.get("title"), (String) map.get("content"), 0, 0, new Date(), map.get("token").toString());
            boolean b = iBusinessMessagesCenterService.addBusinessMessages(messageCenter);
            if (!b) {
                return "false";
            }
        }
        return "true";
    }


}
