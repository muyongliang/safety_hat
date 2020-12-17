package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 项目Vo
 */
public class ProjectVo {
    private Long pid;

    private String projectName;

    public ProjectVo() {
    }

    public ProjectVo(Long pid, String projectName) {
        this.pid = pid;
        this.projectName = projectName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
