package com.wyfx.total.license;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 自定义LicenseManager，用于增加额外的服务器硬件信息校验
 */
public class CustomLicenseManager extends LicenseManager {
    private static final Logger logger = LogManager.getLogger(CustomLicenseManager.class);

    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    public CustomLicenseManager() {
    }


    public CustomLicenseManager(LicenseParam param) {
        //调用LicenseManager的构造方法 注入证书相关的参数
        super(param);
    }

    /**
     * 复写create方法  在生成证书的时候调用
     */
    @Override
    protected synchronized byte[] create(
            LicenseContent content,
            LicenseNotary notary)
            throws Exception {
        //初始化许可证内容
        initialize(content);
        //验证license信息的准确性
        this.validateCreate(content);

        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 复写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     *
     * @param key    是从证书中获取的key
     * @param notary 执照公证人  LicenseManager 的 382 和756 通过证书的参数(keyStoreParam)生成的
     */
    @Override
    protected synchronized LicenseContent install(
            final byte[] key,
            final LicenseNotary notary)
            throws Exception {
        //生成通用证书(GenericCertificate)
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        //验证公证人
        notary.verify(certificate);
        //加载并解析license.lic文件
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        //验证license.lic文件的内容 自定义校验内容
        this.validate(content);
        //设置许可证密钥
        setLicenseKey(key);
        //设置证书
        setCertificate(certificate);
        //返回证书内容
        return content;
    }

    /**
     * 重写verify方法，在此方法中调用本类中的validate方法，校验IP地址、Mac地址等自定义信息
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary)
            throws Exception {
        GenericCertificate certificate = getCertificate();

        // Load license key from preferences, 从首选项中加载许可证密钥
        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        certificate = getPrivacyGuard().key2cert(key);

        notary.verify(certificate);
        //加载license.lic文件
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());

        //校验自定义的数据
        this.validate(content);

        setCertificate(certificate);
        return content;
    }

    /**
     * 生成证书时---校验证书的信息
     *
     * @param content 证书正文
     */
    protected synchronized void validateCreate(final LicenseContent content)
            throws LicenseContentException {
        //返回许可证配置参数
        final LicenseParam param = getLicenseParam();
        System.err.println("生成证书时获取到的配置参数==" + param);
        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }


    /**
     * 复写validate方法，增加IP地址、Mac地址等其他信息校验
     */
    @Override
    protected synchronized void validate(final LicenseContent content)
            throws LicenseContentException {
        //1. 首先调用父类的validate方法 校验基本信息
        super.validate(content);

        //2. 然后校验自定义的信息
        //获取license文件的自定义校验信息
        LicenseCheckModel expectedCheckModel = (LicenseCheckModel) content.getExtra();
        //当前服务器真实的自定义校验信息
        LicenseCheckModel serverCheckModel = getServerInfos();
        //执行校验自定义信息
        if (expectedCheckModel != null && serverCheckModel != null) {
            //校验IP地址
            if (!checkIpAddress(expectedCheckModel.getIpAddress(), serverCheckModel.getIpAddress())) {
                throw new LicenseContentException("当前服务器的IP没在授权范围内");
            }
            //校验Mac地址
            if (!checkIpAddress(expectedCheckModel.getMacAddress(), serverCheckModel.getMacAddress())) {
                throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
            }
        } else {
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
    }


    /**
     * 重写XMLDecoder解析XML
     *
     * @param encoded XML类型字符串
     */
    private Object load(String encoded) {
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));

            decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE), null, null);

            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (decoder != null) {
                    decoder.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("XMLDecoder解析XML失败", e);
            }
        }

        return null;
    }

    /**
     * 获取当前服务器需要额外校验的License参数
     */
    private LicenseCheckModel getServerInfos() {
        //操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
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
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
     */
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {
        if (expectedList != null && expectedList.size() > 0) {
            if (serverList != null && serverList.size() > 0) {
                for (String expected : expectedList) {
                    if (serverList.contains(expected.trim())) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
     */
    private boolean checkSerial(String expectedSerial, String serverSerial) {
        if (StringUtils.isNotBlank(expectedSerial)) {
            if (StringUtils.isNotBlank(serverSerial)) {
                return expectedSerial.equals(serverSerial);
            }

            return false;
        } else {
            return true;
        }
    }

}
