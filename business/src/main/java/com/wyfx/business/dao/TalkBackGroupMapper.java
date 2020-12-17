package com.wyfx.business.dao;

import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.vo.TalkBackGroupVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkBackGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TalkBackGroup record);

    int insertSelective(TalkBackGroup record);

    TalkBackGroup selectByPrimaryKey(Long id);

    TalkBackGroup selectActivateByProjectId(@Param("projectId") Long projectId);

    int updateByPrimaryKeySelective(TalkBackGroup record);

    int updateByPrimaryKey(TalkBackGroup record);

    /**
     * 根据bid和对讲组名称查询
     *
     * @param projectId
     * @param name
     * @return
     */
    TalkBackGroup selectByNameAndProjectId(@Param("projectId") Long projectId, @Param("name") String name);

    int updateByActivate(@Param("projectId") Long projectId, @Param("isActivate") Integer isActivate);

    int updateByActivateAndId(@Param("id") Long id, @Param("isActivate") Integer isActivate);

    int updateByIsLock(@Param("id") Long id, @Param("isLock") Integer isLock);

    List<TalkBackGroupVo> selectByProjectId(@Param("projectId") Long projectId);

    List<TalkBackGroupVo> selectByBusinessId(@Param("businessId") Long businessId);

    List<TalkBackGroupVo> selectByProjectIdAndName(@Param("projectId") Long projectId, @Param("name") String name);

}