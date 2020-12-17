package com.wyfx.business.controller.ws.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/20
 * @description WebSocket传输命令
 */
public class DataBean implements Serializable {

    private String createTime;
    private String des;
    private List<MemberBean> member;
    /**
     * my : {"id":"123","name":"张三","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","state":"1","accountType":"z"}
     * member : [{"memberId":"memberId","memberName":"memberName","state":"0","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","accountType":"z"}]
     * target : [{"id":"123","name":"targetName","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","accountType":"d","state":"1"}]
     * room : {"roomId":"-1","roomName":"房间1","roomUrl":"roomUrl"}
     * des : 终端发起语音呼叫
     * createTime : 12345678
     */

    private Account my;
    private RoomBean room;
    private List<Account> target;

    /**
     * eventName : invited
     * type : z2d
     * data : {"my":{"id":"123","name":"张三","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","state":"1","accountType":"z"},"member":[{"memberId":"memberId","memberName":"memberName","state":"0","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","accountType":"z"}],"target":[{"id":"123","name":"targetName","headUrl":"https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg","accountType":"d","state":"1"}],"room":{"roomId":"-1","roomName":"房间1","roomUrl":"roomUrl"},"des":"终端发起语音呼叫","createTime":"12345678"}
     */
    /*public static class DataBean implements Serializable {*/
    public DataBean() {
    }

    public DataBean(String createTime, String des, List<MemberBean> member, Account my, RoomBean room, List<Account> target) {
        this.createTime = createTime;
        this.des = des;
        this.member = member;
        this.my = my;
        this.room = room;
        this.target = target;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<MemberBean> getMember() {
        return member;
    }

    public void setMember(List<MemberBean> member) {
        this.member = member;
    }

    public Account getMy() {
        return my;
    }

    public void setMy(Account my) {
        this.my = my;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public List<Account> getTarget() {
        return target;
    }

    public void setTarget(List<Account> target) {
        this.target = target;
    }

    public static class MemberBean {

        private String accountType;
        private String headUrl;
        /**
         * memberId : memberId
         * memberName : memberName
         * state : 0
         * headUrl : https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg
         * accountType : z
         */

        private String memberId;
        private String memberName;
        private String state;

        public MemberBean() {
        }

        public MemberBean(String accountType, String headUrl, String memberId, String memberName, String state) {
            this.accountType = accountType;
            this.headUrl = headUrl;
            this.memberId = memberId;
            this.memberName = memberName;
            this.state = state;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class RoomBean {
        /**
         * roomId : -1
         * roomName : 房间1
         * roomUrl : roomUrl
         */

        private String roomId;
        private String roomName;
        private String roomUrl;

        public RoomBean() {
        }

        public RoomBean(String roomId, String roomName, String roomUrl) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.roomUrl = roomUrl;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRoomUrl() {
            return roomUrl;
        }

        public void setRoomUrl(String roomUrl) {
            this.roomUrl = roomUrl;
        }
    }

        /*public static class TargetBean {
            private String accountType;
            private String headUrl;
            *//**
     * id : 123
     * name : targetName
     * headUrl : https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg
     * accountType : d
     * state : 1
     *//*

            private String id;
            private String name;
            private String state;

            public TargetBean() {
            }

            public TargetBean(String accountType, String headUrl, String id, String name, String state) {
                this.accountType = accountType;
                this.headUrl = headUrl;
                this.id = id;
                this.name = name;
                this.state = state;
            }

            public String getAccountType() {
                return accountType;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getState() {
                return state;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setState(String state) {
                this.state = state;
            }
        }*/

}
