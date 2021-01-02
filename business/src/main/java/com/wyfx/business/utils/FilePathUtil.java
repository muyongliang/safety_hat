package com.wyfx.business.utils;


import com.wyfx.business.controller.commons.SafetyHatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     *
     * @param type            对讲/广播等类型
     * @param businessAccount 企业主账号
     * @return
     */
    public static String getFileSavePath(Integer type, String businessAccount) {
        String typeStr = null;
        switch (type) {
            case 1:
                typeStr = UserTypeAndStatus.broadcastPath;
                break;
            case 2:
                typeStr = UserTypeAndStatus.talkBackPath;
                break;
            case 3:
                typeStr = UserTypeAndStatus.patrolRecordPath;
                break;
            case 4:
                typeStr = UserTypeAndStatus.phoneRecordPath;
                break;
        }
        String applicationAddr = FilePathUtil.getExcuteJarPath();
        String date = DateUtil.getCurrentDate();
//        String uploadPath=applicationAddr+File.separator+"safety-hat"+File.separator+businessAccount+File.separator+date+File.separator+typeStr+File.separator;
        String uploadPath = applicationAddr + File.separator + "safety-hat" + File.separator + businessAccount + File.separator + typeStr + File.separator + date + File.separator;
        return uploadPath;
    }

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
        StringBuffer stringBuffer = new StringBuffer();
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";//ip地址的正则表达式

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(request.getServerName());
        if (m.matches()) {
            //匹配到IP
            stringBuffer.append("http://");
            stringBuffer.append(request.getServerName());
            stringBuffer.append(":");
            stringBuffer.append(request.getServerPort());
        } else {
            //匹配到域名或 localhost
            if (!request.getServerName().equals("localhost")) {
                stringBuffer.append("https://");
                stringBuffer.append(request.getServerName());
            } else {
                stringBuffer.append("http://");
                stringBuffer.append(request.getServerName());
                stringBuffer.append(":");
                stringBuffer.append(request.getServerPort());
            }
        }
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

    /**
     * 删除某一文件或某一目录
     * 并返回删除的文件名集合
     *
     * @param file
     */
    public static List<String> deleteFile(File file) {
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null) {
                    return fileNames;
                }
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteFile(files[i]);
                    }
                    Files.delete(files[i].toPath());
                    String name = files[i].getName();
                    fileNames.add(name);
                }
            } else {
                file.deleteOnExit();
                fileNames.add(file.getName());
                System.out.println("文件删除成功");
            }
        } catch (Exception e) {
            logger.error("文件/文件夹删除失败", e);
        }
        return fileNames;
    }

    /**
     * 文件下载
     *
     * @param url
     * @param request
     * @param response
     */
    public static void download(String url, HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = FilePathUtil.getExcuteJarPath() + File.separator + url.substring(url.indexOf("safety-hat"));
            File file = new File(path);

            String fileName = file.getName();
            OutputStream outputStream = null;
            if (file != null) {
                byte[] bytes = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                int len, offSet = 0;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    /*fileInputStream.read(bytes,offSet,len);*/
                }
                String userAgent = request.getHeader("User-Agent");
                byte[] filenameBytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes(StandardCharsets.UTF_8);
                fileName = new String(filenameBytes, StandardCharsets.ISO_8859_1);

                response.setCharacterEncoding("UTF-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
            } else {
                throw new SafetyHatException("No corresponding client was found");
            }
        } catch (Exception e) {
            throw new SafetyHatException("文件下载失败:找不到该文件");
        }
    }

}
