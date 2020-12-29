package com.wyfx.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.alarmRange.util.TrackUtils;
import com.wyfx.business.app.service.ContactService;
import com.wyfx.business.app.vo.MemberDetailVo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BugLog;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.FileInfo;
import com.wyfx.business.entity.vo.MultipartFileParam;
import com.wyfx.business.service.FileService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.IBugLogService;
import com.wyfx.business.utils.FilePathUtil;
import com.wyfx.business.utils.FileUploadUtil;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;


/**
 * @author johnson liu
 * @date 2019/12/3
 * @description 数据管理
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Value("${slice.size}")
    private String sliceSize;

    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private IBugLogService iBugLogService;
    @Autowired
    private IExcelService iExcelService;

    @RequestMapping(value = "/getGps", method = RequestMethod.GET)
    public Object getGps(String userName) {
        BusinessUser user = businessUserService.findByUserName(userName);
        MemberDetailVo memberDetailVo = contactService.findMemberInfo(user.getBid(), user.getUserType());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), memberDetailVo);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)

//    @RequiresPermissions("file:download")

    public Object download(Integer fid, HttpServletRequest request, HttpServletResponse response) {
        //http://192.168.0.16:8083\\safety-hat\\sxlc\\2019-12-21\\broadcast\\1576911516217.wav
        try {
            String url = fileService.getByFid(fid.longValue()).getUrl();
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
                return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "No corresponding client was found");
            }
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "文件下载失败:找不到该文件");
        }
        return null;
    }

    /**
     * 上传图片和视频文件的断点续传
     *
     * @param param
     * @return
     */
    @AopLog(describe = "上传数据：", targetParamName = "", operationType = OperationType.INSERT)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(MultipartFileParam param, HttpServletRequest request) {
        BusinessUser user = getCurrentUser();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String url = "";
        if (isMultipart) {
            System.out.println("上传文件start");
            String latitude = param.getLatitude();
            String longitude = param.getLongitude();
            TrackUtils.checkLatLng(Double.valueOf(latitude), Double.valueOf(longitude));//检查经纬度是否有效
            boolean flag = true;
            try {
                String fileName = param.getFileName();
                int isPic = checkPic(fileName);
                /*boolean isChange=MergeFile.verifyFile(param.getMd5(), param.getFile().getInputStream());//验证MD5值,检查文件是否被改变
                if(!isChange && isPic==1){
                    System.out.println("文件发生改变:"+param.getMd5());
                    return  new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(),"分片文件发生改变");
                }*/
                String path = null;
                isPic = (param.getType() == null) ? isPic : param.getType();
                switch (isPic) {
                    case 0:
                        return new MyResponseEntity(ResponseCode.FAILD_UPLOAD.getValue(), "错误的文件格式");
                    case 1:
                        path = FileUploadUtil.uploadFile(user.getUserName(), param);
                        System.out.println("上传成功,文件保存地址:" + path);
                        break;
                    case 2:
                    case 3:
                        JSONObject jsonObject = FileUploadUtil.uploadVideo(user.getUserName(), Long.valueOf(sliceSize) * 1024 * 1024, param);
                        if (!String.valueOf(Byte.MAX_VALUE).equals(jsonObject.getString("isComplete"))) {
                            flag = false;
                        } else {
                            path = jsonObject.getString("path");
                        }
                        break;
                }
                if (flag) {
                    if (isPic == 1 || isPic == 2) {
                        //更新数据库文件记录
                        FileInfo fileInfo = new FileInfo(null, param.getFileName(), user.getUserName(), user.getName(), user.getUserType(), param.getSize(), new Date(), isPic, user.getProjectId(), path);
                        fileService.addFileInfo(fileInfo);
                    }
                    url = path;
                }
            } catch (Exception e) {
                logger.error("文件上传失失败", e.getMessage());
                param.setFile(null);
                return new MyResponseEntity(ResponseCode.FAILD_UPLOAD.getValue(), param);
            }
            System.out.println("上传文件end");
        }
        param.setFile(null);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), url, param);
    }

    /**
     * 按账号类型(userType)分页查询照片或视频文件列表
     *
     * @param type     文件类型
     * @param userType 上传用户类型(账号类型)
     * @return
     */
    @RequestMapping(value = "/getListByType", method = RequestMethod.POST)
    public Object getListByType(Long projectId, Integer type, Integer userType, Integer pageSize, Integer pageNum) {
        PageInfo<FileInfo> pageInfo = null;
        if (projectId == null) {
            BusinessUser user = getCurrentUser();
            projectId = user.getProjectId();
        }
        if (userType != null && (userType == 1 || userType == 2)) {
            pageInfo = fileService.getListByType(projectId, type, userType, pageSize, pageNum);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
        }
        pageInfo = fileService.getList(projectId, type, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 获取数据模块视频/图片的链接
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
//    @RequiresPermissions("file:detail")
    public Object detail(@PathVariable("id") Long id, HttpServletRequest request) {
        String url = fileService.getUrlById(id, request);
        if (url == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "没有该文件");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), url);
    }

    /**
     * 按时间段分页查询照片或视频文件列表
     *
     * @param type
     * @param begin
     * @param end
     * @return
     */
    @RequestMapping(value = "/getListByUploadTime", method = RequestMethod.POST)
    public Object getListByUploadTime(Long projectId, Integer type, String begin, String end, Integer pageSize, Integer pageNum) {
        try {
            PageInfo<FileInfo> pageInfo = fileService.getListByUploadTime(projectId, type, begin, end, pageSize, pageNum);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }

    }

    /**
     * 根据文件名称查询照片或视频
     *
     * @param projectId
     * @param type
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/getListByFileName", method = RequestMethod.POST)
    public Object getListByFileName(Long projectId, Integer type, String fileName, Integer pageSize, Integer pageNum) {
        PageInfo<FileInfo> pageInfo = fileService.getListByFileName(projectId, type, fileName, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    @RequestMapping(value = "/getListByAccount", method = RequestMethod.POST)
    public Object getListByAccount(Long projectId, Integer type, String account, Integer pageSize, Integer pageNum) {
        PageInfo<FileInfo> pageInfo = fileService.getListByAccount(projectId, type, account, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    @RequestMapping(value = "/getListByName", method = RequestMethod.POST)
    public Object getListByName(Long projectId, Integer type, String name, Integer pageSize, Integer pageNum) {
        PageInfo<FileInfo> pageInfo = fileService.getListByName(projectId, type, name, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    @RequestMapping(value = "/editFileName", method = RequestMethod.POST)
//    @RequiresPermissions("file:edit")
    @AopLog(describe = "修改文件名称：", targetParamName = "fid", operationType = OperationType.UPDATE)
    public Object editFileName(Long fid, String fileName) {
        fileService.updateFileName(fid, fileName);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @AopLog(describe = "删除数据：", targetParamName = "fid", operationType = OperationType.DELETE)
//    @RequiresPermissions("file:delete")
    public Object deleteFile(Long fid) {
        fileService.deleteFile(fid);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/deleteFiles", method = RequestMethod.POST)
//    @RequiresPermissions("file:delete")
    @AopLog(describe = "删除数据：", targetParamName = "fids", operationType = OperationType.DELETE)
    public Object deleteFile(String fids) {
        fileService.deleteFile(fids);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 通过文件名检查是否是图片
     *
     * @param fileName
     * @return
     */
    private int checkPic(String fileName) {
        String str = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (str.equalsIgnoreCase("jpg") || str.equalsIgnoreCase("jpeg") || str.equalsIgnoreCase("png")) {
            return UserTypeAndStatus.pic;
        } else if (str.equalsIgnoreCase("mp4")) {
            return UserTypeAndStatus.video;
        } else {
            return UserTypeAndStatus.nonsupport;
        }
    }

    /**
     * 上传App日志 崩溃日志
     *
     * @param content  日志内容字符串
     * @param type     1调度员 2终端
     * @param username 上传用户
     * @return
     */
    @PostMapping("/uploadAppLog")
    public Object uploadAppLog(String content, String type, String username) {
//           写出日志到txt文件
//        String businessAccount = username.substring(0, username.indexOf("_"));//获取企业主账号
//        String fileName;
//        Integer userType;
//        if (Integer.parseInt(type) == 1) {
//            fileName = "disLog";
//            userType = 1;
//        } else {
//            userType = 2;
//            fileName = "clientLog";
//        }
//        String applicationAddr = FilePathUtil.getExcuteJarPath();
//        String currentDate = DateUtil.getCurrentDate();
//        //上传文件保存路径
//        String uploadPath = applicationAddr + File.separator + "safety-hat" + File.separator + businessAccount + File.separator + "log" + File.separator + currentDate + File.separator + username;
//        //用户访问路径
//        String filePath = File.separator + "log" + File.separator + currentDate + File.separator + username + File.separator + fileName;
//        FileWriter fwriter = null;
//        //文件保存路径
//        try {
//            //创建保存日志的文件夹
//            File file1 = new File(uploadPath);
//            if (!file1.exists()) {
//                file1.mkdirs();
//            }
//            //创建保存文件  true表示不覆盖原来的内容，而是加到文件的后面。
//            fwriter = new FileWriter(file1 + File.separator + fileName, true);
//            fwriter.write(content);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return new MyResponseEntity(ResponseCode.FAILD_UPLOAD.getValue(), "上传失败");
//        } finally {
//            try {
//                fwriter.flush();
//                fwriter.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                return new MyResponseEntity(ResponseCode.FAILD_UPLOAD.getValue(), "上传失败");
//            }
//        }


        //保存日志信息到数据库供总后台查询并导出txt文件给web端  businessId 为business_user 中的bid
        Long bid = businessUserService.findByUserName(username).getBid();
        BugLog bugLog = new BugLog(null, username, Integer.parseInt(type), bid.intValue(), content);
        logger.info("app上传崩溃日志" + bugLog);
        iBugLogService.addBugLog(bugLog);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "上传成功");
    }

    /**
     * 通过账号id 查询所有app崩溃日志记录
     *
     * @param bid 用户在business_user 中的bid
     * @return
     */
    @PostMapping("/handleGetAppBugLogs")
    public String handleGetAppBugLogs(@RequestBody String bid) {
        logger.info("总后台导出app日志id" + bid);
        List<String> strings = iBugLogService.selectSomeOneAllBugLog(Integer.valueOf(JSON.parse(bid).toString()));
        logger.info("总后台导出app日志" + strings.size());
        if (strings.size() == 0) {
            return "false";
        }
        String s = JSON.toJSONString(strings);
        return s;
    }

    /**
     * 导出图片数据
     *
     * @param fileIds
     * @return
     */
    @PostMapping("/exportImg")
//    @RequiresPermissions("file:exportImg")
    public MyResponseEntity exportImg(String fileIds, HttpServletResponse response) {
        List<Long> list = JSONArray.parseArray(fileIds).toJavaList(Long.class);
        //文件类型{1:照片;2:视频}
        List<FileInfo> fileInfos = fileService.selectFileList(list, 1);
        iExcelService.exportExcel(fileInfos, FileInfo.class, "图片数据", response);
        return new MyResponseEntity(200);

    }

    /**
     * 导出视频数据
     *
     * @param fileIds
     * @return
     */
    @PostMapping("/exportVideo")
//    @RequiresPermissions("file:exportVideo")
    public MyResponseEntity exportVideo(String fileIds, HttpServletResponse response) {
        List<Long> list = JSONArray.parseArray(fileIds).toJavaList(Long.class);
        //文件类型{1:照片;2:视频}
        List<FileInfo> fileInfos = fileService.selectFileList(list, 2);
        iExcelService.exportExcel(fileInfos, FileInfo.class, "视频数据", response);
        return new MyResponseEntity(200);

    }


}
