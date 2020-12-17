package com.wyfx.total.mapper;

import com.wyfx.total.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    /**
     * 通过父id查询子项目列表
     *
     * @param parentId
     * @return
     */
    List<Area> selectListByParentId(Integer parentId);


    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);
}