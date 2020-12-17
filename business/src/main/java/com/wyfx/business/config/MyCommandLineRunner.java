package com.wyfx.business.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.alarmRange.util.HttpClientUtil;
import com.wyfx.business.entity.BusinessInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.ZidianSetting;
import com.wyfx.business.service.common.IBusinessInfoService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.ZidianServer;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * @Author johnliu
 * @create 2018/8/31 20:12
 * @Description 在Spring容器加载完成后执行，可防止applicationContext为null
 * 当有多个CommandLineRunner接口实现的时候，可以通过@Order(value = 1)来指定加载顺序，值越小，越先加载
 **/
/*@Component
@Order(value = 1)*/
@Deprecated
public class MyCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

    @Value("${third.run}")
    private String third;
    @Value("${remote.url}")
    private String remoteUrl;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IBusinessInfoService iBusinessInfoService;
    @Autowired
    private ZidianServer zidianServer;


    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        //获取该主机的的IP和Mac并验证
        if ("true".equals(third)) {
            String mac = NetworkUtil.getLocalMac();
            String result = HttpClientUtil.doPostJson("http://" + remoteUrl + "/authorizationManagement/handleVerificationTBusiness", JSON.toJSONString(mac));
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("status").equals("false")) {
                logger.error("mac地址未授权");
                System.exit(0);//正常退出应用程序
            } else {
                BusinessInfo businessInfo = jsonObject.getJSONObject("businessInfo").toJavaObject(BusinessInfo.class);
                //todo 添加第三方企业账号信息
                iBusinessInfoService.addBusinessInfo(businessInfo);
                BusinessUser businessUser = new BusinessUser();
                businessUser.setUserName(businessInfo.getMainAccount());//账号名
                businessUser.setPassword(MD5Util.encrypt(businessInfo.getMainAccount(), businessInfo.getMainAccount()));//根据用户名密码加密返回加密后的密码
                businessUser.setToken(businessInfo.getToken());
                businessUser.setBusinessId(businessInfo.getBusinessId());//所属企业的id 即企业信息表(businessInfo)的id
                businessUser.setUserType(0);//企业管理员
                businessUser.setCreatePerson("总后台添加");
                businessUser.setName(businessInfo.getBusinessName());//企业名
                businessUser.setCreateTime(new Date());
                try {
                    businessUserService.addBusinessUser(businessUser);
                    //ZidianSetting zidian=new ZidianSetting(zidianType.longValue(),workName,status,color,businessUser.getBid());
                    ZidianSetting moren = new ZidianSetting(0L, "默认工种", 0, "#00cc00", businessUser.getBusinessId());
                    zidianServer.addWorkType(moren);
                } catch (Exception e) {
                    logger.error("mac地址未授权");
                }
            }
        }
        //每次启动时将所有设备的状态改为离线状态
        //new Thread(new TcpListener()).start();
    }
}
