package com.wyfx.business.dao;

import com.wyfx.business.app.vo.MemberDetailVo;
import com.wyfx.business.entity.Gps;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsMapper {
    int deleteByPrimaryKey(Long gid);

    int insert(Gps record);

    int insertSelective(Gps record);

    Gps selectByPrimaryKey(Long gid);

    int updateByPrimaryKeySelective(Gps record);

    int updateByPrimaryKey(Gps record);

    Gps selectByClientId(Long clientId);

    /**
     * 查询终端成员定位信息
     *
     * @param userId
     * @return
     */
    MemberDetailVo selectClientInfoByUserId(@Param("userId") Long userId);

    /**
     * 查询调度员定位信息
     *
     * @param userId
     * @return
     */
    MemberDetailVo selectDispatcherInfoByUserId(@Param("userId") Long userId);
}