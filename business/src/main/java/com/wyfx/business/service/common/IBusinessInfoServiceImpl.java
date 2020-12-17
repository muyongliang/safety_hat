package com.wyfx.business.service.common;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.BusinessInfoMapper;
import com.wyfx.business.dao.BusinessUserMapper;
import com.wyfx.business.entity.BusinessInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.utils.ConstantList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * create by wsm on 2019-12-5
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IBusinessInfoServiceImpl implements IBusinessInfoService {

    private static final Logger logger = LoggerFactory.getLogger(IBusinessInfoServiceImpl.class);

    @Autowired
    private BusinessInfoMapper businessInfoMapper;
    @Autowired
    private BusinessUserMapper businessUserMapper;

    /**
     * 总后台 添加企业信息
     *
     * @param businessInfo
     * @return
     */
    @Override
    @Transactional
    public boolean addBusinessInfo(BusinessInfo businessInfo) {
        int i = businessInfoMapper.insertSelective(businessInfo);
        if (i < 0) {
            System.out.println("总后台向企业后台插入企业数据失败，联系管理员");
            return false;
        }
        return true;
    }

    /**
     * 总后台 更新企业信息
     *
     * @param businessInfo
     * @return
     */
    @Override
    @Transactional
    public boolean updateBusinessInfo(BusinessInfo businessInfo) {
        int i = businessInfoMapper.updateByToken(businessInfo);

        List<BusinessUser> list = businessUserMapper.findByTokenAndBusinessId(businessInfo.getToken());
        Map<String, javax.websocket.Session> map = null;
        String message = JSON.toJSONString(new BaseCommand(WsConstant.logout.name(), "", null));
        javax.websocket.Session webSession = null;
        javax.websocket.Session androidSession = null;
        for (BusinessUser businessUser : list) {
            map = ConstantList.sessionMap.get(businessUser.getUserName());
            webSession = (map == null) ? null : map.get("web");
            androidSession = (map == null) ? null : map.get("android");

            try {
                if (webSession != null) {
                    webSession.getBasicRemote().sendText(message);//发送退出命令
                }
                if (androidSession != null) {
                    androidSession.getBasicRemote().sendText(message);//发送退出命令
                }
                logger.info("暂停合作时发送退出命令:" + businessUser.getUserName());
            } catch (Exception e) {
                logger.error("暂停合作时退出之前账号失败", e);
            }

        }
        if (i < 0) {
            System.out.println("总后台向企业后台更新企业数据失败，联系管理员");
            return false;
        }
        return true;
    }

    @Override
    public BusinessInfo findByBusinessId(Long businessId) {
        return businessInfoMapper.selectByPrimaryKey(businessId);
    }

    /**
     * create by wsm on 2020-1-8
     *
     * @param token
     * @return
     */
    @Override
    public BusinessInfo findByToken(String token) {
        BusinessInfo businessInfoByToken = businessInfoMapper.findBusinessInfoByToken(token);
        return businessInfoByToken;
    }

    /**
     * 批量修改到期企业的合作状态
     *
     * @param tokes
     * @return
     */
    @Override
    public boolean updateBusinessStatusByTokens(List<String> tokes) {
        //合作状态{0:合作中;1:暂停合作}
        int i = businessInfoMapper.updateBusinessStatus(tokes, 1);
        if (i < 0) {
            System.out.println("总后台向企业后台更新企业数据失败，联系管理员");
            return false;
        }
        return true;
    }
}
