package com.wyfx.business.controller;

import java.util.List;

/**
 * 百度报警信息
 */
public class BaiduAlarmResponse {


    /**
     * type : 2
     * service_id : 2741
     * content : [{"fence_id":1,"fence_name":"A_01","monitored_person":"A001","action":"enter","alarm_point":{"longitude":113.29251520916,"latitude":23.45610760748,"radius":3,"coord_type":"bd09ll","loc_time":1490179579,"create_time":1490179593},"pre_point":{"longitude":113.20148113236,"latitude":23.431332045033,"radius":10,"coord_type":"bd09ll","loc_time":1490179557,"create_time":1490179569}}]
     */

    private int type;
    private int service_id;
    private List<ContentBean> content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BaiduAlarmResponse{" +
                "type=" + type +
                ", service_id=" + service_id +
                ", content=" + content +
                '}';
    }

    public static class ContentBean {
        /**
         * fence_id : 1
         * fence_name : A_01
         * monitored_person : A001
         * action : enter
         * alarm_point : {"longitude":113.29251520916,"latitude":23.45610760748,"radius":3,"coord_type":"bd09ll","loc_time":1490179579,"create_time":1490179593}
         * pre_point : {"longitude":113.20148113236,"latitude":23.431332045033,"radius":10,"coord_type":"bd09ll","loc_time":1490179557,"create_time":1490179569}
         */

        private int fence_id;
        private String fence_name;
        private String monitored_person;
        private String action;
        private AlarmPointBean alarm_point;
        private PrePointBean pre_point;

        public int getFence_id() {
            return fence_id;
        }

        public void setFence_id(int fence_id) {
            this.fence_id = fence_id;
        }

        public String getFence_name() {
            return fence_name;
        }

        public void setFence_name(String fence_name) {
            this.fence_name = fence_name;
        }

        public String getMonitored_person() {
            return monitored_person;
        }

        public void setMonitored_person(String monitored_person) {
            this.monitored_person = monitored_person;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public AlarmPointBean getAlarm_point() {
            return alarm_point;
        }

        public void setAlarm_point(AlarmPointBean alarm_point) {
            this.alarm_point = alarm_point;
        }

        public PrePointBean getPre_point() {
            return pre_point;
        }

        public void setPre_point(PrePointBean pre_point) {
            this.pre_point = pre_point;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "fence_id=" + fence_id +
                    ", fence_name='" + fence_name + '\'' +
                    ", monitored_person='" + monitored_person + '\'' +
                    ", action='" + action + '\'' +
                    ", alarm_point=" + alarm_point +
                    ", pre_point=" + pre_point +
                    '}';
        }

        public static class AlarmPointBean {
            /**
             * longitude : 113.29251520916
             * latitude : 23.45610760748
             * radius : 3
             * coord_type : bd09ll
             * loc_time : 1490179579
             * create_time : 1490179593
             */

            private double longitude;
            private double latitude;
            private int radius;
            private String coord_type;
            private int loc_time;
            private int create_time;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getRadius() {
                return radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            public String getCoord_type() {
                return coord_type;
            }

            public void setCoord_type(String coord_type) {
                this.coord_type = coord_type;
            }

            public int getLoc_time() {
                return loc_time;
            }

            public void setLoc_time(int loc_time) {
                this.loc_time = loc_time;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            @Override
            public String toString() {
                return "AlarmPointBean{" +
                        "longitude=" + longitude +
                        ", latitude=" + latitude +
                        ", radius=" + radius +
                        ", coord_type='" + coord_type + '\'' +
                        ", loc_time=" + loc_time +
                        ", create_time=" + create_time +
                        '}';
            }
        }

        public static class PrePointBean {
            /**
             * longitude : 113.20148113236
             * latitude : 23.431332045033
             * radius : 10
             * coord_type : bd09ll
             * loc_time : 1490179557
             * create_time : 1490179569
             */

            private double longitude;
            private double latitude;
            private int radius;
            private String coord_type;
            private int loc_time;
            private int create_time;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getRadius() {
                return radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            public String getCoord_type() {
                return coord_type;
            }

            public void setCoord_type(String coord_type) {
                this.coord_type = coord_type;
            }

            public int getLoc_time() {
                return loc_time;
            }

            public void setLoc_time(int loc_time) {
                this.loc_time = loc_time;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            @Override
            public String toString() {
                return "PrePointBean{" +
                        "longitude=" + longitude +
                        ", latitude=" + latitude +
                        ", radius=" + radius +
                        ", coord_type='" + coord_type + '\'' +
                        ", loc_time=" + loc_time +
                        ", create_time=" + create_time +
                        '}';
            }
        }
    }
}
