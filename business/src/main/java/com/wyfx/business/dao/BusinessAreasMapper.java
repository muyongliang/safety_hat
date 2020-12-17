package com.wyfx.business.dao;

import com.wyfx.business.entity.BusinessAreas;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessAreasMapper {
    int deleteByPrimaryKey(String aid);

    int insert(BusinessAreas record);

    int insertSelective(BusinessAreas record);

    BusinessAreas selectByPrimaryKey(String aid);

    int updateByPrimaryKeySelective(BusinessAreas record);

    int updateByPrimaryKey(BusinessAreas record);

    List<BusinessAreas> getAreasByParentId(String parentId);
}