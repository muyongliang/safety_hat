package com.wyfx.business.service.sys;

import com.wyfx.business.entity.BusinessPageSetting;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.DefaultClientVideo;
import com.wyfx.business.entity.ResolutionType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SystemServer {

    void addLogoImg(String name, MultipartFile logoImg, MultipartFile metaImg, BusinessUser businessUser);

    BusinessPageSetting findPageSettingByBusinessId(long businessId, HttpServletRequest request);

    List<ResolutionType> getResolutionType();

    void addVideoParamSetting(Integer type, Integer bitRate, BusinessUser user);

    DefaultClientVideo findByBusinessId(Long businessId);

    void destroyClientData(Long clientId, Integer[] type) throws Exception;

    void changeClientAccount(Long beforeClientId, Long afterClientId) throws Exception;

    void synchronizeVideoParam(Long businessId, Integer resolution) throws Exception;

    void synchronizeVideoParam2(Long businessId, String resolution) throws Exception;

}
