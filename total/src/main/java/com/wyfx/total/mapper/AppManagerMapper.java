package com.wyfx.total.mapper;

import com.wyfx.total.entity.AppManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppManagerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppManager record);

    int insertSelective(AppManager record);

    AppManager selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppManager record);

    int updateByPrimaryKey(AppManager record);

    List<Map> selectAll();


}