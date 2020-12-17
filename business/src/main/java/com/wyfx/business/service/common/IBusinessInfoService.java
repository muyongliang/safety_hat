package com.wyfx.business.service.common;

import com.wyfx.business.entity.BusinessInfo;

import java.util.List;

/**
 * create by wsm on 2019-12-5
 * 总后台添加企业时 在企业后台插入企业数据
 */
public interface IBusinessInfoService {
    boolean addBusinessInfo(BusinessInfo businessInfo);

    boolean updateBusinessInfo(BusinessInfo businessInfo);

    BusinessInfo findByBusinessId(Long businessId);

    boolean updateBusinessStatusByTokens(List<String> tokes);

    BusinessInfo findByToken(String token);

}
