package com.wyfx.business.dao;

import com.wyfx.business.entity.GroupInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupInfoMapper {
    int deleteByPrimaryKey(Long groupId);

    int insert(GroupInfo record);

    int insertSelective(GroupInfo record);

    GroupInfo selectByPrimaryKey(Long groupId);

    int updateByPrimaryKeySelective(GroupInfo record);

    int updateByPrimaryKey(GroupInfo record);

    List<GroupInfo> selectByProjectIdAndName(@Param("projectId") Long projectId, @Param("groupName") String groupName);

    List<GroupInfo> selectByProjectId(@Param("projectId") Long projectId);
}