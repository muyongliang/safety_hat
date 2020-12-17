package com.wyfx.business.alarmRange.core;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 电子围栏、轨迹请求链接;url域名
 */
public class UrlDomain {

    public static final String YINGYAN_HTTP_URL = "http://yingyan.baidu.com/api/v3/";

    public static final String YINGYAN_HTTPS_URL = "https://yingyan.baidu.com/api/v3/";

    /*public static final String ACTION_ADD_POINT = "track/addpoint";

    public static final String ACTION_ADD_POINTS = "track/addpoints";*/

    /**
     * ***************************************************
     * ************** 围栏创建更新接口
     * ***************************************************
     */
    public static final String createCirclefence = "fence/createcirclefence";//创建圆形围栏

    public static final String createPolygonfence = "fence/createpolygonfence";//创建多边形围栏

    public static final String updateCirclefence = "fence/updatecirclefence";//更新圆形围栏

    public static final String updatePolygonfence = "fence/updatepolygonfence";//更新多边形围栏

    public static final String deleteFence = "fence/delete";//删除围栏

    /**
     * ***************************************************
     * ************** 围栏报警接口 GET请求 ***********************
     * ***************************************************
     */
    public static final String queryRangeStatus = "fence/querystatus";//查询监控对象相对围栏的状态
    public static final String querystatusbylocation = "fence/querystatusbylocation";//根据坐标查询监控对象相对围栏的状态
    public static final String historyalarm = "fence/historyalarm";//查询某监控对象的围栏报警信息
    public static final String batchhistoryalarm = "fence/batchhistoryalarm";//批量查询所有围栏报警信息


}
