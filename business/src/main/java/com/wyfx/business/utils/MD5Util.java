package com.wyfx.business.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author johnson liu
 */
public class MD5Util {

    private static final String ALGORITH_NAME = "md5";
    private static final int HASH_ITERATIONS = 5;

    protected MD5Util() {

    }

    public static String encrypt(String username, String password) {
        String source = StringUtils.lowerCase(username);
        /*password = StringUtils.lowerCase(password);
        return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(source), HASH_ITERATIONS).toHex();*/
        return password;
    }


}
