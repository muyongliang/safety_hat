package com.wyfx.business.controller.ws.pojo;

/**
 * @author johnson liu
 * @date 2019/12/13
 * @description 对讲命令
 */
public class TalkbackCmd {
    /**
     * groupId : 3
     * realName : admin_01
     * url : 语音文件url地址
     */

    private String groupId;
    private String realName;
    private String url;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
