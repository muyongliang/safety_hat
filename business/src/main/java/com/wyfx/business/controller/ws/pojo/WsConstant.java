package com.wyfx.business.controller.ws.pojo;

public enum WsConstant {

    /**
     * *********************************************
     * ***********   单人视频通话 ***************
     * *********************************************
     */
    invited,//邀请标识
    beInvited,//被邀请标识
    invitedRes,//邀请回复标识
    beInvitedRes,//被邀请回复
    invitedClose,//邀请结束标识


    /**
     * *********************************************
     * ***********   多人视频通话 ***************
     * *********************************************
     */
    invitedMore,//多人通话邀请标识
    beInvitedMore,//多人通话被邀请标识
    invitedMoreRes,//多人通话邀请回复标识
    beInvitedMoreRes,//多人通话被邀请回复
    invitedMoreClose,//邀请结束标识

    /**
     * *********************************************
     * ***********   单人语音通话 ***************
     * *********************************************
     */
    invitedVoice,//邀请标识
    beInvitedVoice,//被邀请标识
    invitedVoiceRes,//邀请回复标识
    beInvitedVoiceRes,//多人通话被邀请回复
    invitedVoiceClose,//邀请结束标识


    /**
     * *********************************************
     * ***********   多人语音通话 ***************
     * *********************************************
     */
    invitedVoiceMore,//多人通话邀请标识
    beInvitedVoiceMore,//多人通话被邀请标识
    invitedVoiceMoreRes,//多人通话邀请回复标识
    beInvitedVoiceMoreRes,//多人通话被邀请回复
    invitedVoiceMoreClose,//邀请结束标识
    /**
     * *********************************************
     * ***********   呼叫排队命令 ***************
     * *********************************************
     */
    beInvitedVoiceBusy,//被叫人语音通话中
    beInvitedBusy,//被叫人视频通话中


    d2z,//调度员-终端
    d2d,//调度员-调度员
    d2a,//调度员-所有端
    z2d,//终端-调度员
    z,//终端账号类型
    d,//调度员账号类型

    bindingSocketId,//绑定socketId标识
    findUserInfoBySocketId,//通过socketId查询用户标识
    operation,//操作标识

    /**
     * *********************************************
     * ***********   会话结束标识 ***************
     * *********************************************
     */
    voiceConversationEnd,
    voiceMoreConversationEnd,
    videoConversationEnd,
    videoMoreConversationEnd,


    /**
     * *********************************************
     * ***********   对讲/广播命令 ***************
     * *********************************************
     */
    talkback,//对讲
    broadcast,//广播
    intercom,//对讲语音类型
    text,//文本

    /**
     * *********************************************
     * ***********   系统配置命令 ***************
     * *********************************************
     */
    destroy,//销毁数据
    changeAccount,//改变终端登录账号
    synchronizeVideoParam,//同步视频参数
    logout,//退出登录命令
    config,
    getLogging,//获取日志
    updateApp,//总后台app更新提示
    switchActivateStatus,
    SwitchServer,//总后台切换服务器
    updateAlarmSetting,//更新报警设置
    getAlarmRecord,//获取报警记录
    /**
     * 更新围栏命令
     */
    updateRange,
    updateOnline,
    heartCheck,
}
