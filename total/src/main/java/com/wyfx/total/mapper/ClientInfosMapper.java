package com.wyfx.total.mapper;

import com.wyfx.total.entity.ClientInfos;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Deprecated
public interface ClientInfosMapper {
    int deleteByPrimaryKey(Long cid);

    int insert(ClientInfos record);

    int insertSelective(ClientInfos record);

    ClientInfos selectByPrimaryKey(Long cid);

    List<ClientInfos> selectByAddressAndBusinessId(String address, String businessId, Integer onlineFlag);

    int updateByPrimaryKeySelective(ClientInfos record);

    int updateByPrimaryKey(ClientInfos record);
}