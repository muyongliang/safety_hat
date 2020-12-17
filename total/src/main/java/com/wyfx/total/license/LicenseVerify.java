package com.wyfx.total.license;

import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 */
public class LicenseVerify {
    private static final Logger logger = LogManager.getLogger(LicenseVerify.class);

    /**
     * 安装License证书
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) throws Exception {
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //1. 安装证书  将异常跑出给调用者处理
//        try{
        //获取定制的证书管理器
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
        //卸载之前的证书
        licenseManager.uninstall();
        //安装新的证书并返回安装情况(在安装的同时已经验证了证书的信息) 调用定制的证书管理器的CustomLicenseManager.install()
        result = licenseManager.install(new File(param.getLicensePath()));
        logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}", format.format(result.getNotBefore()), format.format(result.getNotAfter())));
//        }catch (Exception e){
//            logger.error("证书安装失败！",e);
//        }

        return result;
    }

    /**
     * 校验License证书
     *
     * @return boolean
     */
    public boolean verify() throws Exception {
        //获取并生成自定义的证书管理器
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书  跑出异常给调用者处理
//        try {
        LicenseContent licenseContent = licenseManager.verify();
        logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
        return true;
//        }catch (Exception e){
//            logger.error("证书校验失败！",e);
//            return false;
//        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     * @return LicenseParam
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }

}
