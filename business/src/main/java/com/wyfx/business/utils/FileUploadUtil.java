package com.wyfx.business.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.entity.vo.MultipartFileParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/12/3
 * @description 文件上传工具类
 */
public class FileUploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    /**
     * 上传成功后返回保存的地址
     *
     * @param param
     * @param userName 企业主账号
     * @return
     */
    public static String uploadFile(String userName, MultipartFileParam param) throws Exception {
        /*String fileName = UUID.randomUUID().toString().replaceAll("-", "");*/
        String businessAccount = userName.substring(0, userName.indexOf("_"));//获取企业主账号
        String fileName = param.getFileName();
        //获取文件后缀名
        String ext = FilenameUtils.getExtension(fileName);
        MultipartFile file = param.getFile();
        String applicationAddr = FilePathUtil.getExcuteJarPath();
        String currentDate = DateUtil.getCurrentDate();
        String dirTo = (param.getType() == null) ? "images" : getDirTo(param);
        String uploadPath = applicationAddr + File.separator + "safety-hat" + File.separator + businessAccount + File.separator + dirTo + File.separator + currentDate;
        String filePath = File.separator + "safety-hat" + File.separator + businessAccount + File.separator + dirTo + File.separator + currentDate + File.separator + fileName;
        File tFile = new File(uploadPath + File.separator + fileName);
        if (!tFile.getParentFile().exists()) {
            tFile.getParentFile().mkdirs();
        }
        tFile.createNewFile();
        file.transferTo(tFile);
        return filePath;
    }

    public static JSONObject uploadVideo(String userName, long sliceSize, MultipartFileParam param) throws Exception {
        String businessAccount = userName.substring(0, userName.indexOf("_"));//获取企业主账号
        String currentDate = DateUtil.getCurrentDate();
        String dirTo = (param.getType() == null) ? "video" : getDirTo(param);
        String baseDir = FilePathUtil.getExcuteJarPath() + File.separator + "safety-hat" + File.separator + businessAccount + File.separator + dirTo + File.separator + currentDate + File.separator;
        String md5 = param.getMd5();
        File baseFile = new File(baseDir);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
            System.out.println("创建文件临时目录:" + baseFile.getAbsolutePath());
        }
        File confFile = new File(baseDir + File.separator + md5 + ".conf");
        File tempFile = new File(baseDir + File.separator + md5 + "_temp");
        long offset = (param.getChunk() - 1) * sliceSize;
        String path = null;
        Map<String, Object> map = new HashMap<>();
        try {
            RandomAccessFile accessTmpFile = new RandomAccessFile(tempFile, "rw");
            RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
            accessTmpFile.seek(offset);//定位到该分片的偏移量
            accessTmpFile.write(param.getFile().getBytes());//将数据写入到临时文件

            accessConfFile.setLength(param.getChunks());
            accessConfFile.seek(param.getChunk() - 1);
            accessConfFile.write(Byte.MAX_VALUE);

            //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            byte isComplete = Byte.MAX_VALUE;
            for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
                //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
                isComplete = (byte) (isComplete & completeList[i]);
            }

            accessTmpFile.close();
            accessConfFile.close();
            if (isComplete == Byte.MAX_VALUE) {
                //上传完成，修改文件名称
                /*String suffix= fileInfoService.getFileSuffix(flag);*/
                /*renameFile(tempFile,flag+suffix);*/
                /*int index= param.getFileName().lastIndexOf(".");*/
                long length = tempFile.length();
                map.put("fileSize", length);
                path = renameFile(tempFile, param.getFileName());
                confFile.delete();//删除零时配置文件
                System.out.println("文件上传成功,保存路径:" + path);
            }
            map.put("isComplete", String.valueOf(isComplete));
            map.put("path", path);
        } catch (Exception e) {
            logger.error("文件上传失败:" + e);
        }
        return (JSONObject) JSON.toJSON(map);
    }

    /**
     * 文件重命名
     *
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    public static String renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("File does not exist: " + toBeRenamed.getName());
            return null;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        toBeRenamed.renameTo(newFile);
        String baseDir = FilePathUtil.getExcuteJarPath();
        String filePath = newFile.getAbsolutePath();
        int index = filePath.indexOf(baseDir);
        return filePath.substring(index + baseDir.length());
    }

    public static String byteToMb(long size) {
        String suffix = "";
        float fSzie = 0;
        if (size >= 1024) {
            suffix = "KB";
            fSzie = size / 1024;
            if (fSzie >= 1024) {
                suffix = "MB";
                fSzie /= 1024;
                if (fSzie >= 1024) {
                    suffix = "GB";
                    fSzie /= 1024;
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#0.00");// 字符显示格式
        /* 每3个数字用,分隔，如1,000 */
        formatter.setGroupingSize(3);
        StringBuilder resultBuffer = new StringBuilder(formatter.format(fSzie));
        if (suffix != null) {
            resultBuffer.append(suffix);
        }
        return resultBuffer.toString();
    }

    public static String getDirTo(MultipartFileParam param) {
        Integer type = param.getType();
        String dirTo = null;
        if (type != null) {
            switch (type) {
                case 1:
                    dirTo = UserTypeAndStatus.imagesPath;
                    break;
                case 2:
                    dirTo = UserTypeAndStatus.videoPath;
                    break;
                case 3:
                    dirTo = UserTypeAndStatus.phoneRecordPath;
                    break;
            }
        }
        return dirTo;
    }

    /**
     * 检查并修改文件上传进度
     *
     * @param param
     * @param uploadDirPath
     * @return
     * @throws IOException
     */
    private boolean checkAndSetUploadProgress(MultipartFileParam param, String uploadDirPath) throws IOException {
        String fileName = param.getFileName();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            System.out.println("check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        /*if (isComplete == Byte.MAX_VALUE) {
            stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "true");
            stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName);
            return true;
        } else {
            if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, param.getMd5())) {
                stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            }
            if (stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + param.getMd5())) {
                stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName + ".conf");
            }
            return false;
        }*/
        return false;
    }
}
