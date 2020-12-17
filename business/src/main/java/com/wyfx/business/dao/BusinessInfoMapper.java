package com.wyfx.business.dao;

import com.wyfx.business.entity.BusinessInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessInfoMapper {
    int deleteByPrimaryKey(Long businessId);

    int insert(BusinessInfo record);

    int insertSelective(BusinessInfo record);

    BusinessInfo selectByPrimaryKey(Long businessId);

    int updateByPrimaryKeySelective(BusinessInfo record);

    int updateByPrimaryKey(BusinessInfo record);

    int updateByToken(BusinessInfo record);

    BusinessInfo findBusinessInfoByToken(String token);

    int updateBusinessStatus(@Param("tokens") List<String> tokens, @Param("status") Integer status);

    /**
     * 计算该企业的终端数量
     *
     * @param token
     * @return
     */
    int selectClientNumByBusinessId(@Param("token") String token);

}