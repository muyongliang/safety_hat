package com.wyfx.business.dao;

import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.entity.AlarmRangeMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRangeMemberMapper {
    int deleteByPrimaryKey(Long mid);

    int insert(AlarmRangeMember record);

    int insertSelective(AlarmRangeMember record);

    AlarmRangeMember selectByPrimaryKey(Long mid);

    AlarmRangeMember selectByRidAndBid(@Param("rid") Long rid, @Param("bid") Long bid);

    int updateByPrimaryKeySelective(AlarmRangeMember record);

    int updateByPrimaryKey(AlarmRangeMember record);

    List<ContactsVo> selectByRid(@Param("rid") Long rid, @Param("status") Integer status);

    List<ContactsVo> selectNotInAlarmRangeClient(@Param("rid") Long rid, @Param("status") Integer status, @Param("projectId") Long projectId);

    /**
     * 批量插入数据
     *
     * @param members
     * @return
     */
    int insertByForeach(@Param("members") List<AlarmRangeMember> members);

    /**
     * 批量修改数据
     *
     * @param members
     * @return
     */
    int updateByForeach(@Param("rid") Long rid, @Param("status") Integer status, @Param("members") List<AlarmRangeMember> members);
}