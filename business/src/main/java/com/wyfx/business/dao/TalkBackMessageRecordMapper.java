package com.wyfx.business.dao;

import com.wyfx.business.entity.TalkBackMessageRecord;

public interface TalkBackMessageRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TalkBackMessageRecord record);

    int insertSelective(TalkBackMessageRecord record);

    TalkBackMessageRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TalkBackMessageRecord record);

    int updateByPrimaryKeyWithBLOBs(TalkBackMessageRecord record);

    int updateByPrimaryKey(TalkBackMessageRecord record);
}