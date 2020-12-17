package com.wyfx.business.utils;

/**
 * 用户类型
 */
public interface UserTypeAndStatus {
    /**
     * ************************* 用户类型*******************
     */
    int BUSINESS_MANAGER = 0;//企业管理员
    int DISPATCHER = 1;//调度员
    int CLIENT_ACCOUNT = 2;//终端账号
    int OTHER_MANAGER = 3;//其他管理员
    /**
     * <------------------------ 用户类型---------------------->
     */

    /**
     * ************************* 用户在线状态*******************
     */
    int OFF_LINE = 0;//离线
    int MOBILE_ONLINE = 1;//手机在线
    int WEB_ONLINE = 2;//电脑在线
    int WEB_MOBILE_ONLINE = 3;//手机电脑同时在线
    /**
     * <------------------------ 用户在线状态---------------------->
     */
    /**
     * 启用状态
     */
    int account_Enable_status = 1;
    /**
     * 禁用状态
     */
    int account_not_Enable_status = 0;
    /**
     * 项目进行中
     */
    int project_starting = 1;
    /**
     * 项目已结束
     */
    int project_end = 2;

    /*不支持的文件格式*/
    int nonsupport = 0;
    /**
     * 照片
     */
    int pic = 1;
    /**
     * 视频
     */
    int video = 2;

    int mp3 = 3;

    /**
     * 日志
     */
    int log = 3;

    /**
     * 定义上传文件保存目录
     */
    String imagesPath = "images";//图片
    String videoPath = "video";//视频
    String logPath = "log";//日志
    String talkBackPath = "talkBack";//对讲
    String phoneRecordPath = "phoneRecord";//电话记录
    String broadcastPath = "broadcast";//广播
    String patrolRecordPath = "patrolRecord";//巡检目录

    String sourceOfAndroid = "android";
    String sourceOfWeb = "web";

}
