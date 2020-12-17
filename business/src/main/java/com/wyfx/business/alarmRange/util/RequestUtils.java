package com.wyfx.business.alarmRange.util;

import com.wyfx.business.alarmRange.core.UrlDomain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * http工具类
 *
 * @author baidu
 */
public class RequestUtils {

    /**
     * 编码
     */
    public static final String CHAR_ENC = "UTF-8";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_GET = "GET";
    /**
     * 是否开启HTTPS
     */
    public static boolean isEnableHttps = false;

    public static String sendRequest(String action, Map<String, String> parameters, String method) {
        String urlConnection = null;
        if (isEnableHttps) {
            urlConnection = initHttps(action);
        } else {
            urlConnection = initHttp(action);
        }
        String result = null;
        switch (method) {
            case METHOD_POST:
                result = HttpClientUtil.doPost(urlConnection, parameters);
                break;
            case METHOD_GET:
                result = HttpClientUtil.doGet(urlConnection, parameters);
                break;
            default:
                break;
        }
        return result;
    }

    public static String initHttp(String action) {
        StringBuilder path = new StringBuilder(UrlDomain.YINGYAN_HTTP_URL);
        path.append(action);
        return path.toString();
    }

    public static String initHttps(String action) {
        StringBuilder path = new StringBuilder(UrlDomain.YINGYAN_HTTPS_URL);
        path.append(action);
        return path.toString();
    }

    /**
     * url编码
     *
     * @param parameter
     * @return
     */
    public static String urlEncode(String parameter) {
        try {
            return URLEncoder.encode(parameter, CHAR_ENC);
        } catch (UnsupportedEncodingException e) {
            return parameter;
        }
    }

}
