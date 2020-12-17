package com.wyfx.total.mapper;

import com.wyfx.total.entity.Projects;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Deprecated
public interface ProjectsMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(Projects record);

    int insertSelective(Projects record);

    Projects selectByPrimaryKey(Long pid);

    List<Projects> selectByAddressAndBusinessId(String address, String businessId, Integer status);

    int updateByPrimaryKeySelective(Projects record);

    int updateByPrimaryKey(Projects record);
}