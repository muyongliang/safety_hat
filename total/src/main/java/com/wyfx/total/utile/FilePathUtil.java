package com.wyfx.total.utile;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Author johnliu
 * @create 2019/12/09 21:29
 * @Description 本类用于解决以下问题:
 * 将项目导出为jar包，然后用java -jar 运行时，项目中文件上传的功能无法正常运行
 **/
public class FilePathUtil {
    private static final Logger logger = LoggerFactory.getLogger(FilePathUtil.class);

    /**
     * 获取jar文件的同级目录下保存上传文件的文件夹路径/未使用
     *
     * @return
     */
    public static String getJarUploadPath() {
        String str = null;
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            System.out.println("path:" + path.getAbsolutePath());
            //如果上传目录为/static/images/upload/，则可以如下获取：
            /*File upload = new File(path.getAbsolutePath(),"static/images/upload/");*/
            File upload = new File(path.getAbsolutePath(), "static/images/upload/");
            if (!upload.exists()) {
                upload.mkdirs();
            }
            System.out.println("upload url:" + upload.getAbsolutePath());
            str = upload.getAbsolutePath();
        } catch (Exception e) {
            logger.error("获取Jar路径异常:", e);
        }
        return str;
    }

    /**
     * 读取jar中resources下的文件,并在jar文件外复制一个文件
     *
     * @param fileName     resources下的文件相对路径
     * @param copyFilePath 复制文件的存放路径
     * @return
     */
    public static void getJarFileInputStream(String fileName, String copyFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = FilePathUtil.class.getResourceAsStream(fileName);
            File file = new File(copyFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            byte[] bytes = new byte[1024];
            String name = fileName.substring(fileName.lastIndexOf("/") + 1);
            FileOutputStream fileOutputStream = new FileOutputStream(copyFilePath + "/" + name);
            int num = 0;
            while ((num = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, num);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            logger.error("操作jar文件异常：", e);
        }
    }

    /**
     * 获取运行jar的路径
     *
     * @return
     */
    public static String getExcuteJarPath() {
        ApplicationHome home = new ApplicationHome(FilePathUtil.class);
        File jarFile = home.getSource();
        String parent = (jarFile == null) ? "" : jarFile.getParent();//获取到jar的存放路径
        System.out.println("parent:" + parent);
        return parent;
    }

    /**
     * 获取上传保存目录
     * @param type 对讲/广播等类型
     * @param businessAccount 企业主账号
     * @return
     */
//    public static String getFileSavePath(Integer type,String businessAccount){
//        String typeStr=null;
//        switch (type){
//            case 1:
//                typeStr="broadcast";
//                break;
//            case 2:
//                typeStr="talkBack";
//                break;
//            case 3:
//                typeStr="patrolRecord";
//                break;
//        }
//        String applicationAddr=FilePathUtil.getExcuteJarPath();
//        String date= DateUtil.getCurrentDate();
//        String uploadPath=applicationAddr+File.separator+"safety-hat"+File.separator+businessAccount+File.separator+date+File.separator+typeStr+File.separator;
//        return uploadPath;
//    }

    /**
     * 获取企业logo文件保存路径
     *
     * @param businessAccount
     * @return
     */
    public static String getLogoFileUploadPath(String businessAccount) {
        String typeStr = "logo";
        String applicationAddr = FilePathUtil.getExcuteJarPath();
        String uploadPath = applicationAddr + File.separator + "safety-hat" + File.separator + businessAccount + File.separator + typeStr + File.separator;
        return uploadPath;
    }

    /**
     * 获取基础baseUrl
     *
     * @param request
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request) {
        StringBuffer stringBuffer = new StringBuffer("http://");
        stringBuffer.append(request.getServerName());
        stringBuffer.append(":");
        stringBuffer.append(request.getServerPort());
        stringBuffer.append(request.getContextPath());
        return stringBuffer.toString();
    }

    /**
     * 保存上传文件
     *
     * @param filePath 文件的绝对路径
     * @param file     上传文件
     * @return 返回文件相对于部署位置的存储路径
     */
    public static String uploadFile(String filePath, MultipartFile file) throws Exception {
        String path = null;
        try {
            File tFile = new File(filePath);
            if (!tFile.getParentFile().exists()) {
                tFile.getParentFile().mkdirs();
            }
            tFile.createNewFile();
            file.transferTo(tFile);
            String jarPath = FilePathUtil.getExcuteJarPath();
            path = filePath.substring(filePath.indexOf(jarPath) + jarPath.length());
        } catch (Exception e) {
            throw e;
        }
        return path;
    }

    /**
     * 获取语音文件的时长
     *
     * @param file 音频文件
     * @return
     */
    public static Long getVoiceTime(File file) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip.open(ais);
            return clip.getMicrosecondLength();
        } catch (Exception e) {
            logger.error("获取语音文件时长失败", e);
            return null;
        }
    }

}
