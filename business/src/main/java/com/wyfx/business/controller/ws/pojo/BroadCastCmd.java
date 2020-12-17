package com.wyfx.business.controller.ws.pojo;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/12/13
 * @description 广播命令
 */
public class BroadCastCmd {

    /**
     * target : [{"userName":"admin_01"},{"userName":"admin_01"},{"userName":"admin_01"}]
     * content : 这是一条语音广播！
     */

    private String content;
    private List<TargetBean> target;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TargetBean> getTarget() {
        return target;
    }

    public void setTarget(List<TargetBean> target) {
        this.target = target;
    }

    public static class TargetBean {
        /**
         * userName : admin_01
         */
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
