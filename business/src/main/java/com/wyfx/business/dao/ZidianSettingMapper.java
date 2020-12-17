package com.wyfx.business.dao;

import com.wyfx.business.entity.ZidianSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZidianSettingMapper {
    int deleteByPrimaryKey(Long zid);

    int insert(ZidianSetting record);

    int insertSelective(ZidianSetting record);

    ZidianSetting selectByPrimaryKey(Long zid);

    int updateByPrimaryKeySelective(ZidianSetting record);

    int updateByPrimaryKey(ZidianSetting record);

    int updateWorkTypeStatus(@Param("zid") int zid, @Param("status") int status, @Param("orderNum") Integer orderNum);

    int updateStatusAndOrderNum(@Param("zid") int zid, @Param("status") int status);

    List<ZidianSetting> findPageByWorkType(@Param("zidianType") long zidianType, @Param("businessId") Long businessId, @Param("status") Long status);

    Integer findMaxOrderNum(@Param("businessId") Long businessId);

    /**
     * 将原来的排序字段值 changedNum 改变为现在的 orderNum
     *
     * @param zid
     * @param changedNum
     * @param orderNum
     */
    void updateByOrderNum(@Param("zid") Long zid, @Param("changedNum") Integer changedNum, @Param("orderNum") Integer orderNum);

    ZidianSetting selectByName(@Param("businessId") Long businessId, @Param("name") String name);
}