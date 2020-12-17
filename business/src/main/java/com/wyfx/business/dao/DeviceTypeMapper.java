package com.wyfx.business.dao;

import com.wyfx.business.entity.DeviceType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceType record);

    int insertSelective(DeviceType record);

    DeviceType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceType record);

    int updateByPrimaryKey(DeviceType record);

    List<DeviceType> selectAll();

    int updateByUUIDSelective(DeviceType deviceType);

    DeviceType selectByUUID(String uuid);


}