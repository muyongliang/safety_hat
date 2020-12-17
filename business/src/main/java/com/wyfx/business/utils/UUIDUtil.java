package com.wyfx.business.utils;

import java.util.UUID;

/**
 * @author johnson liu
 * @date 2019/11/3
 * @description 获取32位UUID值
 */
public class UUIDUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
