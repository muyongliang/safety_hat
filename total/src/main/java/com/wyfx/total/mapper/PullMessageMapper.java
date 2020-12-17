package com.wyfx.total.mapper;

import com.wyfx.total.entity.PullMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public interface PullMessageMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(PullMessage record);

    int insertSelective(PullMessage record);

    PullMessage selectByPrimaryKey(Long pid);

    /**
     * 通过标题筛选查询推送消息列表
     *
     * @param title
     * @return
     */
    List<Map> selectByTitle(String title);

    /**
     * 查询所有的推送消息 按更新时间降序排列
     *
     * @return
     */
    List<Map> selectAllPullMessage();

    /**
     * 通过发送标识查询某时间段类需要推送的消息
     *
     * @param flag
     * @param startTime
     * @param endTime
     * @return
     */
    List<PullMessage> selectAllPullMessageByCondition(@Param("flag") Integer flag, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Integer selectPullMessageCount();

    int updateByPrimaryKeySelective(PullMessage record);

    int updateByPrimaryKeyWithBLOBs(PullMessage record);

    int updateByPrimaryKey(PullMessage record);

    /**
     * 更新定时推送消息的标识为已发送
     *
     * @param pullMessageList
     * @return
     */
    int updateFlagByKeyListSelective(@Param("pullMessageList") List<PullMessage> pullMessageList, @Param("flag") Integer flag,
                                     @Param("updatePerson") String updatePerson, @Param("utime") Date utime);
}