package com.wyfx.total.mapper;

import com.wyfx.total.entity.Dispatchers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatchersMapper {
    int deleteByPrimaryKey(Long did);

    int insert(Dispatchers record);

    int insertSelective(Dispatchers record);

    Dispatchers selectByPrimaryKey(Long did);

    List<Dispatchers> selectByAddressAndBusinessId(@Param("address") String address, @Param("businessId") String businessId, @Param("status") Integer status);

    int updateByPrimaryKeySelective(Dispatchers record);

    int updateByPrimaryKey(Dispatchers record);
}