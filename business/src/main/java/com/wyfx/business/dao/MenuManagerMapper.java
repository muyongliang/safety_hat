package com.wyfx.business.dao;

import com.wyfx.business.entity.MenuManager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuManagerMapper {
    int deleteByPrimaryKey(Long mid);

    int insert(MenuManager record);

    int insertSelective(MenuManager record);

    MenuManager selectByPrimaryKey(Long mid);

    int updateByPrimaryKeySelective(MenuManager record);

    int updateByPrimaryKeyWithBLOBs(MenuManager record);

    int updateByPrimaryKey(MenuManager record);

    /**
     * @param parentId
     * @param bid
     * @param type     类型 0菜单 1按钮
     * @return
     */
    List<MenuManager> selectByParentIdAndBid(@Param("parentId") int parentId, @Param("bid") Long bid, @Param("type") int type);


    List<MenuManager> selectByParentId(@Param("parentId") int parentId, @Param("type") int type);

    /**
     * 查询mid通过功能配置的remark标识
     *
     * @return mid 的集合
     */
    List<Integer> selectByFunctionRemark();
}