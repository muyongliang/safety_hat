package com.wyfx.business.utils;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 对分片文件进行合并以及验证文件的完整性(通过MD5)
 */
public class MergeFile {


    /**
     * 通过MD5验证文件的完整性
     *
     * @param md5
     * @param file
     * @return
     */
    public static boolean verifyFile(String md5, File file) {
        boolean flag = false;
        try {
            InputStream inputStream = new FileInputStream(file);
            if (DigestUtils.md5DigestAsHex(inputStream).equals(md5)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 通过MD5验证文件的完整性
     *
     * @param md5         文件MD5值
     * @param inputStream 文件流
     * @return
     */
    public static boolean verifyFile(String md5, InputStream inputStream) {
        boolean flag = false;
        try {
            if (DigestUtils.md5DigestAsHex(inputStream).equals(md5)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public static String getMd5(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            String str = DigestUtils.md5DigestAsHex(inputStream);
            return str;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取字符串的MD5值
     *
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        try {
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
            return md5DigestAsHex;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        /*try {
            Boolean str= MergeFile.verifyFile("34322ba9b5c0a73462bfcf814d5d2d94",new File("D:\\svn\\safety_hat\\business\\target\\safety-hat\\images\\20191204162616.jpg"));
            System.out.println(str);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        Object message = "{\"data\":\"离线\",\"eventName\":\"broadcast\",\"type\":\"text\"}";
        BaseCommand sendCmd = JSON.parseObject(message.toString(), BaseCommand.class);
        System.out.println(sendCmd);
    }


}
