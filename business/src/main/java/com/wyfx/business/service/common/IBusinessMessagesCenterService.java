package com.wyfx.business.service.common;

import com.wyfx.business.entity.BusinessMessageCenter;

import java.util.List;
import java.util.Map;

/**
 * create by wsm on 2019-12-10
 * 用于总后台向企业后台推送消息
 */
public interface IBusinessMessagesCenterService {
    boolean addBusinessMessages(BusinessMessageCenter messageCenter);

    Map selectAllBusinessMessages(Integer pageNum, Integer pageSize);

    List<Long> selectAllUnReadMessages();


    boolean updateBusinessMessagesFlagByMid(List<Long> mids);
}
