package com.wyfx.business.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.*;
import com.wyfx.business.entity.*;
import com.wyfx.business.utils.ConstantList;
import com.wyfx.business.utils.FilePathUtil;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/12/14
 * @description 系统设置业务处理
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemServerImpl implements SystemServer {

    @Autowired
    private BusinessPageSettingMapper businessPageSettingMapper;
    @Autowired
    private ResolutionTypeMapper resolutionTypeMapper;
    @Autowired
    private DefaultClientVideoMapper defaultClientVideoMapper;
    @Autowired
    private BusinessUserMapper businessUserMapper;
    @Autowired
    private ClientAccountMapper clientAccountMapper;
    @Autowired
    private BusinessInfoMapper businessInfoMapper;


    @Override
    @Transactional
    public void addLogoImg(String name, MultipartFile logoImg, MultipartFile metaImg, BusinessUser businessUser) {
        try {
            BusinessPageSetting businessPageSetting = businessPageSettingMapper.selectByBusinessId(businessUser.getBusinessId());
            String uploadPath = FilePathUtil.getLogoFileUploadPath(businessUser.getUserName());

            String logoImgPath = FilePathUtil.uploadFile(uploadPath + logoImg.getOriginalFilename(), logoImg);
            String metaImgPath = FilePathUtil.uploadFile(uploadPath + metaImg.getOriginalFilename(), metaImg);
            BusinessPageSetting before = null;
            if (businessPageSetting == null) {
                businessPageSetting = new BusinessPageSetting();
            } else {
                before = new BusinessPageSetting();
                BeanUtils.copyProperties(businessPageSetting, before);
            }
            businessPageSetting.setSystemName(name);
            businessPageSetting.setBusinessId(businessUser.getBusinessId());
            businessPageSetting.setLogoImg(logoImgPath);
            businessPageSetting.setMetaImg(metaImgPath);
            if (businessPageSetting.getPid() == null) {
                businessPageSettingMapper.insertSelective(businessPageSetting);
            } else {
                businessPageSettingMapper.updateByPrimaryKey(businessPageSetting);
                //删除原来保存的文件
                String jarPath = FilePathUtil.getExcuteJarPath();
                File logoFile = new File(jarPath + File.separator + before.getLogoImg());
                File metaFile = new File(jarPath + File.separator + before.getMetaImg());
                logoFile.delete();
                metaFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BusinessPageSetting findPageSettingByBusinessId(long businessId, HttpServletRequest request) {
        BusinessPageSetting businessPageSetting = businessPageSettingMapper.selectByBusinessId(businessId);
        BusinessPageSetting after = new BusinessPageSetting();
        if (businessPageSetting != null) {
            BeanUtils.copyProperties(businessPageSetting, after);
            String baseUrl = FilePathUtil.getBaseUrl(request);
            after.setLogoImg(baseUrl + businessPageSetting.getLogoImg());
            after.setMetaImg(baseUrl + businessPageSetting.getMetaImg());
        }
        return after;
    }

    @Override
    public List<ResolutionType> getResolutionType() {
        return resolutionTypeMapper.selectAll();
    }

    @Override
    @Transactional
    public void addVideoParamSetting(Integer type, Integer bitRate, BusinessUser user) {
        DefaultClientVideo defaultClientVideo = defaultClientVideoMapper.selectByBusinessId(user.getBusinessId());
        if (defaultClientVideo == null) {
            defaultClientVideo = new DefaultClientVideo();
        }
        defaultClientVideo.setBusinessId(user.getBusinessId());
        defaultClientVideo.setResolution(type);
        defaultClientVideo.setResolutionStr(resolutionTypeMapper.selectByPrimaryKey(type.longValue()).getType());
        defaultClientVideo.setMinKps(bitRate);
        defaultClientVideo.setMaxKps(bitRate);
        if (defaultClientVideo.getVid() == null) {
            defaultClientVideoMapper.insertSelective(defaultClientVideo);
        } else {
            defaultClientVideoMapper.updateByPrimaryKeySelective(defaultClientVideo);
        }
    }

    @Override
    public DefaultClientVideo findByBusinessId(Long businessId) {
        DefaultClientVideo defaultClientVideo = defaultClientVideoMapper.selectByBusinessId(businessId);
        if (defaultClientVideo != null) {
            defaultClientVideo.setResolutionStr(resolutionTypeMapper.selectByPrimaryKey(defaultClientVideo.getResolution().longValue()).getType());
        }
        return defaultClientVideo;
    }

    /**
     * 销毁终端数据
     *
     * @param clientId
     * @param type     {1:"图片";2:"视频";3:"日志"}
     */
    @Override
    public void destroyClientData(Long clientId, Integer[] type) throws Exception {
        try {
            BusinessUser businessUser = businessUserMapper.selectByPrimaryKey(clientId);
            if (businessUser.getOnlineStatus() == 0) {
                throw new Exception("操作失败,设备离线");
            }
            //-- 构造BaseCommand参数
            Map<String, Integer[]> map = new HashMap<>();
            map.put("type", type);
            ConstantList.sendMessage(businessUser.getUserName(), JSONObject.toJSONString(ConstantList.buildBaseCommand(map, WsConstant.destroy.name(), "")));
        } catch (SafetyHatException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @param beforeClientId 当前登录账号
     * @param afterClientId  待替换的终端账号
     * @throws Exception
     */
    @Override
    public void changeClientAccount(Long beforeClientId, Long afterClientId) throws Exception {
        try {
            BusinessUser beforeBusinessUser = businessUserMapper.selectByPrimaryKey(beforeClientId);
            BusinessUser afterUser = businessUserMapper.selectByPrimaryKey(afterClientId);
            //-- 构造BaseCommand
            /*Long deviceId= clientAccountService.findByBid(afterUser.getBid().intValue()).getDeviceId();*/
            Long deviceId = clientAccountMapper.selectByBid(afterUser.getBid()).getDeviceId();
            if (deviceId != null && deviceId > 0) {
                throw new Exception("该账号已经绑定了设备,请重新选择");
            }
            Map<String, String> map = new HashMap<>();
            map.put("userName", afterUser.getUserName());
            map.put("password", afterUser.getPassword());
            ConstantList.sendMessage(beforeBusinessUser.getUserName(), JSONObject.toJSONString(ConstantList.buildBaseCommand(map, WsConstant.changeAccount.name(), "")));
        } catch (SafetyHatException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void synchronizeVideoParam(Long businessId, Integer resolutionType) throws Exception {
        try {
            BusinessInfo businessInfo = businessInfoMapper.selectByPrimaryKey(businessId);

            // 构造BaseCommand
            DefaultClientVideo defaultClientVideo = findByBusinessId(businessId);
            if (defaultClientVideo == null) {
                throw new SafetyHatException("请先设置视频参数");
            }
            String resolution = resolutionTypeMapper.selectByPrimaryKey(resolutionType.longValue()).getType();
            /*String resolution=defaultClientVideo.getResolutionStr();*/

            Map<String, Object> map = new HashMap<>();
            map.put("maxKps", defaultClientVideo.getMaxKps());
            map.put("width", resolution.substring(0, resolution.indexOf("*")));
            map.put("height", resolution.substring(resolution.indexOf("*") + 1));
            WebSocketServer.sendAllMessage(JSONObject.toJSONString(ConstantList.buildBaseCommand(map, WsConstant.synchronizeVideoParam.name(), "")),
                    UserTypeAndStatus.CLIENT_ACCOUNT, "android", businessInfo.getToken());

            //修改终端所有的分辨率为默认配置
            clientAccountMapper.updateClientVideoSetting(businessId, 0, resolution);
        } catch (SafetyHatException e) {
            throw new SafetyHatException(e.getMessage());
        }
    }

    @Override
    public void synchronizeVideoParam2(Long businessId, String resolution) throws Exception {
        try {
            BusinessInfo businessInfo = businessInfoMapper.selectByPrimaryKey(businessId);

            // 构造BaseCommand
            DefaultClientVideo defaultClientVideo = findByBusinessId(businessId);
            if (defaultClientVideo == null) {
                throw new SafetyHatException("请先设置视频参数");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("maxKps", defaultClientVideo.getMaxKps());
            map.put("width", resolution.substring(0, resolution.indexOf("*")));
            map.put("height", resolution.substring(resolution.indexOf("*") + 1));
            WebSocketServer.sendAllMessage(JSONObject.toJSONString(ConstantList.buildBaseCommand(map, WsConstant.synchronizeVideoParam.name(), "")),
                    UserTypeAndStatus.CLIENT_ACCOUNT, "android", businessInfo.getToken());

        } catch (SafetyHatException e) {
            throw new SafetyHatException(e.getMessage());
        }

    }
}
