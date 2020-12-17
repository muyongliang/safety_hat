package com.wyfx.business.dao;

import com.wyfx.business.entity.ResolutionType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolutionTypeMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(ResolutionType record);

    int insertSelective(ResolutionType record);

    ResolutionType selectByPrimaryKey(Long rid);

    int updateByPrimaryKeySelective(ResolutionType record);

    int updateByPrimaryKey(ResolutionType record);

    List<ResolutionType> selectAll();
}