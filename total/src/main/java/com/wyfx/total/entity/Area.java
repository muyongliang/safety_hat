package com.wyfx.total.entity;

public class Area {
    private Integer id;

    private String areaname;

    private Integer parentid;

    private String shortname;

    private String lng;

    private String lat;

    private Integer level;

    private String position;

    private Integer sort;

    public Area(Integer id, String areaname, Integer parentid, String shortname, String lng, String lat, Integer level, String position, Integer sort) {
        this.id = id;
        this.areaname = areaname;
        this.parentid = parentid;
        this.shortname = shortname;
        this.lng = lng;
        this.lat = lat;
        this.level = level;
        this.position = position;
        this.sort = sort;
    }

    public Area() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? null : areaname.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", areaname='" + areaname + '\'' +
                ", parentid=" + parentid +
                ", shortname='" + shortname + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", level=" + level +
                ", position='" + position + '\'' +
                ", sort=" + sort +
                '}';
    }
}