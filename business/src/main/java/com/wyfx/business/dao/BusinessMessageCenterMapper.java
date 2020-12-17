package com.wyfx.business.dao;

import com.wyfx.business.entity.BusinessMessageCenter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BusinessMessageCenterMapper {
    int deleteByPrimaryKey(Long mid);

    int insert(BusinessMessageCenter record);

    int insertSelective(BusinessMessageCenter record);

    BusinessMessageCenter selectByPrimaryKey(Long mid);

    int updateByPrimaryKeySelective(BusinessMessageCenter record);

    int updateByPrimaryKey(BusinessMessageCenter record);

    int updateBusinessMessageFlag(@Param("mids") List<Long> mids);

    /**
     * 通过标识和类型查询消息集合
     *
     * @param flag 消息读取状态{0:未读取;1:已读取}
     * @param type 消息类型{0:官方消息;其余值为报警类型的值}
     * @return
     */
    List<Map> selectAllMessagesByFlagAndType(@Param("flag") Integer flag, @Param("type") Integer type);
}