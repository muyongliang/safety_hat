package com.wyfx.business.utils;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/11
 * @description 账号验证
 */
public class AccountUtil {

    /**
     * @param accountName 要求名称是3-10的数字\字母\字符
     * @return
     */
    public static Boolean checkAccountName(String accountName) {
        int index = accountName.indexOf("_");
        String name = accountName.substring(index + 1);
        String regular = "^\\S{3,10}$";//定义正则
        return name.matches(regular);
    }

    /**
     * 将对象转为Map
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> convertObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static boolean vilicationPhone(String phone) {
        String regex = "^(1[3-9])\\d{9}$";
        String reg = "^0[0-9]{2,3}-[0-9]{8}";
        boolean flag = true;
        if (phone.contains("-")) {
            boolean matches = phone.matches(reg);
            if (!matches) {
                flag = false;
            }
        } else {
            boolean matches = phone.matches(regex);
            if (!matches) {
                flag = false;
            }
        }
        return flag;
    }
}
