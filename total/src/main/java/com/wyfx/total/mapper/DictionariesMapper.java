package com.wyfx.total.mapper;

import com.wyfx.total.entity.Dictionaries;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DictionariesMapper {
    int deleteByPrimaryKey(Integer did);

    int insert(Dictionaries record);

    int insertSelective(Dictionaries record);

    Dictionaries selectByPrimaryKey(Integer did);

    int updateByPrimaryKeySelective(Dictionaries record);

    int updateByPrimaryKey(Dictionaries record);

    List<Map> selectAllOrderByFlag();

    /**
     * 查询最大的启用数  即最大排序数
     *
     * @return
     */
    int selectMaxEnableNum();

    /**
     * 查询所有排序字段大于此字典排序字段的字典id集合
     *
     * @param orderNum
     * @return
     */
    List<Integer> selectAllOrderNumLargerThanMe(Integer orderNum);

    /**
     * 批量更新所有的排序字段
     *
     * @param dids
     * @return
     */
    int updateAllOrderNum(@Param("dids") List<Integer> dids);

    /**
     * 通过字典名字查询  如T1 S2等
     *
     * @param optionName
     * @return
     */
    Dictionaries selectByOptionName(@Param("optionName") String optionName);

    /**
     * 通过排序名查询  2020-2-26
     *
     * @param orderNum
     * @return
     */
    Dictionaries selectByOrderNum(@Param("orderNum") Integer orderNum);


}