package com.wyfx.business.dao;

import com.wyfx.business.entity.BugLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugLogMapper {
    int deleteByPrimaryKey(Integer bugId);

    int insert(BugLog record);

    int insertSelective(BugLog record);

    BugLog selectByPrimaryKey(Integer bugId);

    int updateByPrimaryKeySelective(BugLog record);

    int updateByPrimaryKeyWithBLOBs(BugLog record);

    int updateByPrimaryKey(BugLog record);

    List<BugLog> selectBugLogListByBusinessId(Integer businessId);
}