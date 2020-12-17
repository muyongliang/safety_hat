package com.wyfx.total.mapper;

import com.wyfx.total.entity.TopMessages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopMessagesMapper {
    int deleteByPrimaryKey(Long mid);

    int insert(TopMessages record);

    int insertSelective(TopMessages record);

    TopMessages selectByPrimaryKey(Long mid);

    /**
     * 通过flag查询所有已读或未读消息
     *
     * @param flag 已读0 未读1
     * @return
     */
    List<TopMessages> selectAllTopMessagesByFlag(Integer flag);


    Integer selectTomMessagesCount(Integer flag);

    int updateByPrimaryKeySelective(TopMessages record);

    int updateByPrimaryKey(TopMessages record);

    int updateByMids(@Param("mids") List<Long> mids);
}