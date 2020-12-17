package com.wyfx.total.controller;

import com.wyfx.total.entity.LogInfo;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.utile.ExcelUtils;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(LogController.class);
    @Autowired
    private ILogInfService iLogInfService;

    /**
     * 查询日志
     *
     * @param taskType  类型 增 删 改    操作类型{0:增加;1:删除;2:更新}
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/handleFindLogInfoByCondition")
    public RespBean handleFindLogInfoByCondition(Integer taskType, String startTime, String endTime, Integer pageNum, Integer pageSize) throws ParseException {
        logger.info("通过不同条件查询日志记录");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map logInfoByCondition = iLogInfService.findLogInfoByCondition(taskType,
                ("".equals(startTime) || startTime == null) ? null : sdf.parse(startTime),
                ("".equals(endTime) || endTime == null) ? null : sdf.parse(endTime),
                pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, logInfoByCondition);
    }

    /**
     * 查询所有日志
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/handleFindAllLogInfo")
    public RespBean handleFindAllLogInfo(Integer pageNum, Integer pageSize) {
        Map allLogInfo = iLogInfService.findAllLogInfo(pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, allLogInfo);
    }


    /**
     * 导出所有日志
     *
     * @return
     */
    @PostMapping("/handleExportLog")
    public RespBean handleExportLog(HttpServletResponse response) {
        logger.info("导出日志记录");
        List list = iLogInfService.selectLogList();
        ExcelUtils.exportExcel(list, null, "sheet1", LogInfo.class, "日志列表" + ".xls", response);
        return new RespBean(ResponseCode.SUCCESS_CODE, "导出成功");
    }

}
