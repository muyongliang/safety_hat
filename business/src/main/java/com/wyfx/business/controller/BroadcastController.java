package com.wyfx.business.controller;

import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.service.RecordManagerService;
import com.wyfx.business.utils.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;


/**
 * @author johnson liu
 * @date 2019/12/12
 * @description 广播控制器
 */
@RestController
@RequestMapping("/broadcast")
public class BroadcastController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BroadcastController.class);

    @Autowired
    private RecordManagerService recordManagerService;

    /**
     * 上传 广播/对讲/巡检记录/通话记录的文件 的语音(图片)文件,并返回文件的访问url
     *
     * @param type
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadVoiceFile", method = RequestMethod.POST)
    public Object uploadVoiceFile(Integer type, MultipartFile file, HttpServletRequest request) {
        if (type == null || file == null) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "非法参数,参数不能为空");
        }
        BusinessUser user = getCurrentUser();
        //获取企业主账号
        String businessAccount = user.getUserName().substring(0, user.getUserName().indexOf("_"));
        String dir = FilePathUtil.getFileSavePath(type, businessAccount);

        String filePath = dir + file.getOriginalFilename();
        System.out.println(filePath);
        String url = null;
        try {
            File tFile = new File(filePath);
            if (!tFile.getParentFile().exists()) {
                tFile.getParentFile().mkdirs();
            }
            tFile.createNewFile();
            file.transferTo(tFile);
            String jarPath = FilePathUtil.getExcuteJarPath();
            String path = filePath.substring(filePath.indexOf(jarPath) + jarPath.length());
            url = FilePathUtil.getBaseUrl(request) + path;
        } catch (Exception e) {
            logger.error("广播/对讲文件上传失败", e);
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "上传失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), url);
    }

    /**
     * 通过记录ID的获取记录详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUrlById", method = RequestMethod.GET)
    public Object getUrlById(Long id, HttpServletRequest request) {
        Map map = null;
        try {
            map = recordManagerService.getUrlById(id, request);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "找不到该记录信息");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


}
