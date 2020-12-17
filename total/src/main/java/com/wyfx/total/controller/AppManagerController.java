package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.wyfx.total.entity.AppManager;
import com.wyfx.total.exception.UploadFileContentTypeException;
import com.wyfx.total.exception.UploadIOException;
import com.wyfx.total.service.IAppManagerService;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wsm
 */
@RestController
@RequestMapping("/appManager")
@Api(tags = "app更新控制器")
public class AppManagerController extends BaseController {


    private static final List<String> FILE_TYPE = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger(AppManagerController.class);
    @Autowired
    private IAppManagerService iAppManagerService;
    @Autowired
    private ILogInfService iLogInfService;
    /**
     * 上传文件保存路径
     */
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    /**
     * 对外暴露访问路径
     */
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

    /**
     * 定义上传文件允许类型
     */
    @PostConstruct
    public void init() {
        FILE_TYPE.add("application/vnd.android.package-archive");
        FILE_TYPE.add("application/octet-stream");
    }


    /**
     * 上传app
     *
     * @param description
     * @param code           1终端app   2调度员app
     * @param client_version 客户端版本
     * @param version        版本
     * @param file
     * @return
     * @throws UploadIOException
     * @throws UploadFileContentTypeException
     */
    @PostMapping(value = "/uploadApp")
    @ApiOperation(value = "App上传更新", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "description", value = "用户名", required = true),
            @ApiImplicitParam(name = "code", value = "app类型  1终端app   2调度员app", required = true),
            @ApiImplicitParam(name = "client_version", value = "客户端版本", required = true),
            @ApiImplicitParam(name = "version", value = "版本", required = true),
            @ApiImplicitParam(name = "file", value = "文件", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "上传app成功"),
            @ApiResponse(code = 209, message = "上传文件类型错误"),
            @ApiResponse(code = 502, message = "读取数据出错！文件可能已被移动、删除，或网络连接中断"),
            @ApiResponse(code = 501, message = "更新企业后台数据失败，联系管理员"),
            @ApiResponse(code = 204, message = "密码错误")
    })
    public RespBean<String> handleAppUpload(String description, Integer code, String client_version, String version,
                                            @RequestParam("file") MultipartFile file, HttpServletRequest request) throws UploadIOException, UploadFileContentTypeException, IOException {
        logger.info("开始上传app");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //限制上传的文件为apk格式
        String contentType = file.getContentType();
        if (!FILE_TYPE.contains(contentType)) {
            throw new UploadFileContentTypeException("上传文件类型错误,允许上传文件类型为：" + FILE_TYPE);
        }
        // 用户上传的文件存储到的文件夹
        File parentDir = new File(uploadFolder);
        // 确保文件夹存在
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        AppManager appManager = new AppManager();
        //获取app相关数据
        String md5 = DigestUtils.md5Hex(file.getBytes());
        appManager.setNewMd5(md5);
        //除法运算保留两个小数
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format((double) file.getSize() / 1024 / 1024);
        appManager.setTargetSize(Double.parseDouble(format));
        //0表示false 1表示true
        appManager.setCons(0);
        // 用户上传的文件存储的文件名
        String fileName;
        //app数据表中只保存2条数据 1终端 2调度员
        if (code == 1) {
            appManager.setId(1L);
            fileName = sdf.format(new Date()) + "_cli_" + client_version + "_" + version + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        } else {
            appManager.setId(2L);
            fileName = sdf.format(new Date()) + "_dis_" + client_version + "_" + version + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        }
        // 确定用户上传的文件在服务器端的路径
        String filePath = staticAccessPath.substring(0, staticAccessPath.lastIndexOf("/") + 1) + fileName;
        // 用户上传的文件存储到服务器端的文件对象
        File dest = new File(parentDir, fileName);
        // 将用户上传的文件存储到指定文件夹
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            logger.error("读取数据出错！文件可能已被移动、删除，或网络连接中断！");
            return new RespBean(502, "读取数据出错！文件可能已被移动、删除，或网络连接中断！");
        }
        //app信息存入数据库
        appManager.setCtime(new Date());
        appManager.setDescription(description);
        appManager.setFileName(file.getOriginalFilename());
        appManager.setUploadTime(new Date());
        appManager.setUrl(filePath);
        appManager.setVersion(version);
        appManager.setClientVersion(client_version);
        appManager.setCreator((String) request.getSession().getAttribute("userName"));
        //添加更新数据到数据库
        boolean b = iAppManagerService.addAppManager(appManager);
        //终端app更新时向企业后台插入版本信息， 企业推送信息需要企业后台主动来查询推送相关的数据
        if (b && appManager.getId() == 1) {
            logger.info("终端app更新时向企业后台插入版本信息");
            String jsonString = JSON.toJSONString(appManager.getClientVersion());
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handlePushClientVersionMessages", jsonString);
            if ("false".equals(s)) {
                logger.error("更新企业后台数据失败，联系管理员");
                return new RespBean(501, "更新企业后台数据失败，联系管理员");
            }
        }
        //添加日志记录
        if (appManager.getId() == 1L) {
            iLogInfService.addLogInfRecord("发布终端更新app", 2, "终端");
        }
        iLogInfService.addLogInfRecord("发布调度员更新app", 2, "调度员");
        logger.info("上传app成功 路径=" + filePath);
        return new RespBean<>(ResponseCode.SUCCESS_CODE, filePath);
    }


    /**
     * 页面刷新 查询所有app信息
     *
     * @return
     */
    @GetMapping("/handleSelectAllAppMessages")
    @ApiOperation(value = "查询app数据", httpMethod = "GET", produces = "form-data")
    public RespBean handleSelectAllAppMessages() {
        logger.info("页面刷新 查询所有app信息");
        List<Map> mapList = iAppManagerService.selectAllAppMessages();
        return new RespBean(ResponseCode.SUCCESS_CODE, mapList);
    }


    /**
     * 企业后台---获取调度员app更新数据
     *
     * @return
     */
    @PostMapping("/getDispatcherAppMessages")
    @ApiIgnore
    public String getDispatcherAppMessages(HttpServletRequest request) {
        logger.info("企业后台--获取app更新数据");
        //调度员
        Long type = 2L;
        Map map = iAppManagerService.selectByType(type, request);
        String jsonString = JSON.toJSONString(map);
        return jsonString;
    }

    /**
     * 企业后台---获取终端app更新数据
     *
     * @param request
     * @return
     */
    @PostMapping("/getClientAppMessages")
    @ApiIgnore
    public String getClientAppMessages(HttpServletRequest request) {
        logger.info("企业后台---获取终端app更新数据");
        //终端
        Long type = 1L;
        Map map = iAppManagerService.selectByType(type, request);
        String jsonString = JSON.toJSONString(map);
        return jsonString;
    }

}
