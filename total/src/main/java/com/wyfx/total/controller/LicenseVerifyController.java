package com.wyfx.total.controller;

import com.alibaba.fastjson.JSONArray;
import com.wyfx.total.exception.UploadFileContentTypeException;
import com.wyfx.total.exception.UploadIOException;
import com.wyfx.total.license.LicenseManagerHolder;
import com.wyfx.total.license.LicenseVerify;
import com.wyfx.total.license.LicenseVerifyParam;
import com.wyfx.total.service.EnterpriseManagementService;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 验证license
 */
@RequestMapping("/verifyLicense")
@RestController
public class LicenseVerifyController extends BaseController {


    private static final List<String> FILE_TYPE = new ArrayList<String>();
    private final Logger logger = LoggerFactory.getLogger(LicenseVerifyController.class);
    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;
    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;
    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;
    /**
     * 证书上传保存路径
     */
    @Value("${license.licensePath}")
    private String licensePath;
    /**
     * 证书上传保存的文件夹
     */
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;
    @Autowired
    private EnterpriseManagementService enterpriseManagementService;

    /**
     * 定义上传文件允许类型
     */
    @PostConstruct
    public void init() {
        FILE_TYPE.add("application/octet-stream");
    }

    /**
     * 上传证书
     */
    @PostMapping("/handleUploadLicense")
    public RespBean handleUploadLicense(@RequestParam("license") MultipartFile license) {
        String contentType = license.getContentType();
        //判断格式是否错误
        if (!FILE_TYPE.contains(contentType)) {
            throw new UploadFileContentTypeException("上传文件类型错误,允许上传文件类型为：" + FILE_TYPE);
        }
        // 用户上传的文件存储到的文件夹
        String licenseUploadFile = licensePath + "uploadFile/";
        File parentDir = new File(licenseUploadFile);
        // 确保文件夹存在
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        // 用户上传的文件存储的文件名
        String fileName = license.getOriginalFilename();
        // 用户上传的文件存储到服务器端的文件对象
        File dest = new File(parentDir, fileName);
        try {
            license.transferTo(dest);
        } catch (IOException e) {
            throw new UploadIOException("读取数据出错！文件可能已被移动、删除，或网络连接中断！");
        }
        //上传成功 更新许可证并保存完整的文件路径
        Map map = new HashMap((int) (1 / 0.75F) + 1);
        map.put("licenseUploadPath", licenseUploadFile + fileName);
        return new RespBean(200, map);

    }

    /**
     * 安装证书license   todo 重启服务器后必须安装证书才能使用
     *
     * @param licenseUploadPath 证书上传路径
     * @return
     */
    @RequestMapping(value = "/handleInstallLicense", method = RequestMethod.GET)
    public RespBean handleInstallLicense(String licenseUploadPath) {

        LicenseContent install = null;
        Map map = new HashMap((int) (2 / 0.75F) + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logger.info("++++++++++++++++++++ 开始安装证书并校验证书 +++++++++++++++++++++++++++++++++++++");
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licenseUploadPath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        LicenseVerify licenseVerify = new LicenseVerify();
        //安装证书
        try {
            install = licenseVerify.install(param);
        } catch (Exception e) {
            //安装证书中出现验证证书信息异常 或者安装异常（如证书已经到期）
            if (e instanceof LicenseContentException) {
                return new RespBean(509, e.getMessage());
            } else {
                return new RespBean(509, "未知异常");
            }
        }
        logger.info("++++++++++++++++++++++++ 证书安装结束 ++++++++++++++++++++++++++++++++++++++");
        //  返回起始时间
        map.put("startT", sdf.format(install.getNotBefore()));
        map.put("endT", sdf.format(install.getNotAfter()));
        return new RespBean(200, map);

    }

    /**
     * 验证许可证是否有效
     * todo 不能在拦截器中验证 验证一次的时间太长3s-4s
     * todo 验证规则 1登录时验证一次 2使用定时任务每天晚上验证一次
     *
     * @return
     */
    @GetMapping("/handleVerifyLicense")
    public RespBean handleVerifyLicense() {
        LicenseVerify licenseVerify = new LicenseVerify();
        //校验证书是否有效
        try {
            licenseVerify.verify();
            return new RespBean(200, "证书有效");
        } catch (Exception e) {
            System.err.println("验证证书失败时的错误信息=" + e.getMessage());
            //todo 处理证书失效  暂停所有的企业 待测试
            //查询所有企业的并筛选出所有企业的bid集合
            List<Map> bidList = enterpriseManagementService.selectAllBusinessASBid();
            List bids = new ArrayList();
            for (int i = 0; i < bidList.size(); i++) {
                String bid = (String) bidList.get(i).get("bid");
                bids.add(bid);
            }
            //暂停所有企业
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handleAutoPauseOperation", JSONArray.toJSONString(bids));
            if ("false".equals(s)) {
                logger.error("批量暂停企业后台合作状态失败 或更新数据失败");
            }
            return new RespBean(506, "证书失效,或证书没有安装");
        }

    }

    /**
     * 获取证书 起始时间 --总后台登录页面使用
     *
     * @return
     */
    @GetMapping("/handleGetLicenseInfo")
    public RespBean handleGetLicenseInfo() {
        Map map = new HashMap((int) (2 / 0.75F) + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
            LicenseContent verify = licenseManager.verify();
            map.put("startT", sdf.format(verify.getNotBefore()));
            map.put("endT", sdf.format(verify.getNotAfter()));
        } catch (Exception e) {
            System.err.println("获取证书起始时间时的错误信息=" + e.getMessage());
            if (e instanceof NullPointerException) {
                return new RespBean(507, "证书未安装或者不存在");
            }
            //证书验证失败 说明证书已经到期
            return new RespBean(508, "证书已到期");
        }
        return new RespBean(200, map);
    }

}
