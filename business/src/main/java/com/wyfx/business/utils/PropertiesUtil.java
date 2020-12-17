package com.wyfx.business.utils;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.DataBean;
import com.wyfx.business.controller.ws.pojo.WsConstant;

import java.util.ResourceBundle;

/**
 * Created by liu on 2018/8/15
 */
public class PropertiesUtil {

    /**
     * 通过key 值获取 propertiesName.properties文件中对应的value值
     *
     * @param key
     * @param propertiesName 属性文件名称
     * @return
     */
    public static String getValueByKey(String key, String propertiesName) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertiesName);
        if (resourceBundle.containsKey(key)) {
            String accountGroup = resourceBundle.getString(key);
            return accountGroup;
        }
        return "";
    }

    public static void main(String[] args) {
        /*PropertiesUtil.getValueByKey("host","host");*/
        String message = JSON.toJSONString(new BaseCommand(WsConstant.updateAlarmSetting.name(), "", new DataBean()));
        System.out.println(message);

    }

}
