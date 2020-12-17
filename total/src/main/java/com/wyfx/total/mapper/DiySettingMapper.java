package com.wyfx.total.mapper;

import com.wyfx.total.entity.DiySetting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DiySettingMapper {
    int deleteByPrimaryKey(Long did);

    int insert(DiySetting record);

    int insertSelective(DiySetting record);

    DiySetting selectByPrimaryKey(Long did);

    List<DiySetting> selectBusinessManager(DiySetting record);

    int updateByPrimaryKeySelective(DiySetting record);

    int updateByPrimaryKey(DiySetting record);

    /**
     * 通过bid(uuid) 查询企业的自定义深圳
     *
     * @param bid
     * @return
     */
    DiySetting selectByBid(String bid);

    /**
     * 查询所有企业的自定义设置
     * 2020-1-8
     *
     * @return
     */
    List<Map> selectAllDiySetting();


}