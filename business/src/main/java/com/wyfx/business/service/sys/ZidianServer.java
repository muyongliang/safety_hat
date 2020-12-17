package com.wyfx.business.service.sys;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.ZidianSetting;

import java.util.List;

public interface ZidianServer {
    /**
     * 添加工种
     */
    void addWorkType(ZidianSetting zidianSetting);

    /**
     * 更新工种启用状态
     *
     * @param zid
     * @param status
     */
    void updateWorkTypeStatus(int zid, int status, Integer businessId);

    /**
     * 更新工种信息
     *
     * @param zidianSetting
     */
    void updateWorkType(ZidianSetting zidianSetting);

    /**
     * 查询该工种下所有工种信息
     *
     * @param pageSize
     * @param pageNum
     * @param zidianType
     * @return
     */
    PageInfo<ZidianSetting> findAll(Integer pageSize, Integer pageNum, int zidianType, Long businessId);

    /**
     * 改变字典排序
     *
     * @param zid      字典ID
     * @param orderNum 字典排序字段
     * @param moveType 移动类型{0:升序(增加排序);1:降序(减少排序)}
     * @return
     */
    void changeZidianOrderNum(Integer zid, Integer orderNum, Integer moveType, Long businessId) throws Exception;

    /**
     * 查询当前最大的排序字段
     *
     * @param businessId
     * @return
     */
    Integer findMaxOrderNum(Long businessId);

    /**
     * 获取工种列表
     *
     * @param zidianType
     * @return
     */
    List<ZidianSetting> getWorkTypeList(int zidianType, Long businessId);

    ZidianSetting findByName(Long businessId, String name);
}
