package com.wyfx.total.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wyfx.total.license.*;
import com.wyfx.total.service.DlbsAuthorizationManagementService;
import com.wyfx.total.utile.RespBean;
import de.schlichtherle.license.LicenseContentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用于生成证书文件，不能放在给客户部署的代码里
 *
 * @author wsm
 * @date 2019/12/26
 * @since 1.0.0
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LicenseCreatorController.class);
    /**
     * 证书生效时间 一般是生成证书的当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final Date issuedTime = new Date();
    /**
     * 用户类型 一般默认参数
     */
    private final String consumerType = "user";
    /**
     * 用户数量 一般默认参数
     */
    private final Integer consumerAmount = 1;
    /**
     * 描述信息 一般默认参数
     */
    private final String description = "";
    /**
     * 证书生成存放路径
     */
    @Value("${license.licensePath}")
    private String licensePath;
    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;
    /**
     * 密钥别称
     */
    @Value("${license.privateAlias}")
    private String privateAlias;
    /**
     * 密钥密码（需要妥善保管，不能让使用者知道）
     */
    @Value("${license.keyPass}")
    private String keyPass;
    /**
     * 访问秘钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;
    /**
     * 密钥库存储路径(私钥和公钥都在一起)
     */
    @Value("${license.privateKeysStorePath}")
    private String privateKeysStorePath;
    /**
     * 对外暴露访问路径
     */
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;
    @Autowired
    private DlbsAuthorizationManagementService dlbsAuthorizationManagementService;

    /**
     * 获取服务器硬件信息----测试用接口
     *
     * @param osName 操作系统类型，如果为空则自动判断
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();
        AbstractServerInfos abstractServerInfos = null;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }
        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * 生成证书需要的参数，如：
     * {"subject":"ccx-models","privateAlias":"privateKey","keyPass":"5T7Zz5Y0dJFcqTxvzkH5LDGJJSGMzQ",
     * "storePass":"3538cef8e7","licensePath":"C:/Users/zifangsky/Desktop/license.lic",
     * "privateKeysStorePath":"C:/Users/zifangsky/Desktop/privateKeys.keystore",
     * "issuedTime":"2018-04-26 14:48:12","expiryTime":"2018-12-31 00:00:00","consumerType":"User",
     * "consumerAmount":1,"description":"这是证书描述信息",
     * "licenseCheckModel":{"ipAddress":["192.168.245.1","10.0.5.22"],
     * "macAddress":["00-50-56-C0-00-01","50-7B-9D-F9-18-41"],"cpuSerial":"BFEBFBFF000406E3",
     * "mainBoardSerial":"L1HF65E00X9"}}
     */
    @RequestMapping(value = "/generateLicense", method = RequestMethod.GET)
    public RespBean generateLicense(String bid, String server_host, String server_mac, String expiryTime) throws ParseException {
        logger.info("--------------------------开始生成许可证书-------------------------------------");
        //额外的服务器硬件校验信息 此处只校验ip 和mac地址
        LicenseCheckModel licenseCheckModel = new LicenseCheckModel();
        List ipList = new ArrayList();
        ipList.add(server_host);
        List macList = new ArrayList();
        macList.add(server_mac);
        licenseCheckModel.setIpAddress(ipList);
        licenseCheckModel.setMacAddress(macList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //许可证到期时间
        Date expiryT = sdf.parse(expiryTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        //1 自定义证书名字格式：  日期_时间_bid.lic
        String licenseName = simpleDateFormat.format(new Date()) + "_" + bid + ".lic";
        File dir = new File(licensePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //生成证书在服务器的保存的绝对路径
        String Path = licensePath + licenseName;
        //添加生成许可证的参数
        LicenseCreatorParam param = new LicenseCreatorParam(
                subject, privateAlias, keyPass, storePass, Path, privateKeysStorePath, issuedTime,
                expiryT, consumerType, consumerAmount, description, licenseCheckModel
        );
        //如果生成许可证时前端没有提供许可证生成后的保存路径 这默认使用配置文件中的保存路径
        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }
        //生成证书返回结果
        boolean result;
        //传入许可证参数
        LicenseCreator licenseCreator = new LicenseCreator(param);
        //生成许可证书
        try {
            result = licenseCreator.generateLicense();
        } catch (Exception e) {
            System.err.println("生成证书错误信息=e.getMessage()==" + e.getMessage());
            if (e instanceof LicenseContentException) {
                return new RespBean(505, e.getMessage());
            }
            return new RespBean(505, "证书文件生成失败！联系管理员");
        }
        if (result) {
            logger.info("-------------------------------证书已经生成-----------------------------");
            Map map = new HashMap(3);
            //证书的可访问路径
            String path = staticAccessPath.substring(0, staticAccessPath.lastIndexOf("/") + 1) + licenseName;
            map.put("licensePath", path);
            //保存路径
            boolean b = dlbsAuthorizationManagementService.updateTBusinessByBid(bid, path);
            if (!b) {
                return new RespBean(505, "证书文件生成失败！联系管理员");
            }
            return new RespBean(200, map);
        } else {
            return new RespBean(505, "证书文件生成失败！联系管理员");
        }
    }


}
