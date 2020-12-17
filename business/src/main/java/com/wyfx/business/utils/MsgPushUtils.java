package com.wyfx.business.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

/**
 * @author johnson liu
 * @date 2019/12/19
 * @description 极光推送工具类
 * 别名（alias）：给指定id的终端设备发送消息。别名需要客户端进行设置
 * 标签（tag）：分组发送消息，理解起来应该类似于微信建了一个群聊的赶脚。标签需要客户端进行设置
 */
public class MsgPushUtils {
    private static final String MASTER_SECRET = "227c09f2375d9ef92ff74cc5";
    private static final String APP_KEY = "4d2ccc3e9531cf2d5370007e";
    protected static Logger logger = LoggerFactory.getLogger(MsgPushUtils.class);
    private static PushPayload pushPayload;//构建推送对象


    /**
     * 推送消息
     *
     * @param payload
     */
    public static void pushMessage(PushPayload payload) {
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
        // For push, all you need do is to build PushPayload object.
        try {
            Gson gson = new Gson();
            String mes = gson.toJson(payload.toJSON().getAsJsonObject());
            System.out.println("Got mes - " + mes);
            PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request", e);
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @param alert 消息内容
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String alert) {
        return PushPayload.alertAll(alert);
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。
     *
     * @param alert    消息内容
     * @param userName 推送目标
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String userName, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(userName))
                .setNotification(Notification.alert(alert))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 projectId 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @param alert     消息内容
     * @param projectId 推送目标(将消息推送给该项目组下所有的调度员)
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(Long projectId, String alert, String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(String.valueOf(projectId)))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }

    /**
     * 自定义推送函数--分组推送;也可使用buildPushObject_android_tag_alertWithTitle()进行推送
     *
     * @param tag     分组标识(此项目中使用项目ID)
     * @param title   消息标题
     * @param content 消息内容
     * @param type    消息通知类型/即报警类型;方便客户端进行页面跳转
     * @return
     */
    public static PushPayload buildPushObject_android_and_iosByTag(String tag, int type, String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.newBuilder()
                        .setAlert(content)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title).setAlertType(1).addExtra("type", type).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtra(title, content).build())
                        .build())
                .build();
    }


    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @param alert    消息内容
     * @param userName 推送目标
     * @param title    标题
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String userName, String alert, String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(userName))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }

    /**
     * 构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，
     * 角标数字为 5，通知声音为 "happy"，并且附加字段 from = "JPush"；消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，
     * 消息是 JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String MSG_CONTENT) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(MSG_CONTENT))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集），
     * 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String MSG_CONTENT) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(MSG_CONTENT)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }


    /**
     * 构建推送对象：推送内容包含SMS信息
     */
    public static void testSendWithSMS() {
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        try {
            SMS sms = SMS.newBuilder()
                    .setDelayTime(1000)
                    .setTempID(2000)
                    .addPara("Test", 1)
                    .build();
            PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "test sms", sms, "alias1");
            System.out.println("Got result - " + result);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }
    }

    public static void main(String[] args) {
        long projectId = 1;
        pushMessage(buildPushObject_android_and_iosByTag(String.valueOf(projectId), 16, "记得给客户的共和国哼哼唧唧的改革和", "SOS报警"));
    }

}
