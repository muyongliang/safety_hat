package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wyfx.total.entity.vo.Log;
import com.wyfx.total.entity.vo.MyClientAccountVo;
import com.wyfx.total.entity.vo.MyDispatcherVo;
import com.wyfx.total.utile.ExcelUtils;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 远程控制
 */
@RestController
@RequestMapping("/remote")
public class RemoteRequestController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(RemoteRequestController.class);

    /**
     * 远程控制 ---按条件查询终端列表
     *
     * @param
     * @return
     */
    @PostMapping("/handleQueryClientByCondition")
    public RespBean handleQueryClientByCondition(MyClientAccountVo myClientAccountVo) {
        logger.info("远程控制 ---按条件查询终端列表");
        //对象转json
        String json = JSON.toJSONString(myClientAccountVo);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/client/handleSelectClientAccountByCondition", json);
        //返回泛型类对象
//        List<MyClientAccountVo> myClientAccountVos = JSON.parseObject(s, new TypeReference<List<MyClientAccountVo>>() {});
        JSONObject jsonObject = JSON.parseObject(s);
        return new RespBean(ResponseCode.SUCCESS_CODE, jsonObject);
    }


    /**
     * 远程控制--按条件查询调度员列表
     *
     * @param
     * @return
     */
    @RequestMapping("/handleSelectDispatcherByCondition")
    public RespBean handleSelectDispatcherByCondition(MyDispatcherVo myDispatcherVo) {
        logger.info("远程控制--按条件查询调度员列表");
        String json = JSON.toJSONString(myDispatcherVo);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/dispatcher/handleSelectDispatcherByCondition", json);
        JSONObject jsonObject = JSON.parseObject(s);
        return new RespBean(ResponseCode.SUCCESS_CODE, jsonObject);
    }


    /**
     * 通过bid查询企业后台日志并导出
     * 暂时不用
     *
     * @param bid
     * @return
     */
    @PostMapping("/handleExportLogsByBid")
    @Deprecated
    public RespBean handleExportLogsByBid(String bid, HttpServletResponse response) {
        logger.info("通过bid导出终端或调度员日志===" + bid);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handleExportLogByBid", JSON.toJSONString(bid));
        List<Log> logList = JSON.parseObject(s, new TypeReference<List<Log>>() {
        });
        logger.info("导出企业后台调度员或终端日志" + logList);
        ExcelUtils.exportExcel(logList, null, "sheet1", Log.class, "日志列表" + ".xls", response);
        return new RespBean(ResponseCode.SUCCESS_CODE, "导出成功");
    }


    /**
     * 远程控制-----导出崩溃日志
     *
     * @param bid
     * @param response
     * @return
     */
    @RequestMapping(value = "/handleExportBugLogsByBid", method = RequestMethod.GET)
    public RespBean handleExportBugLogsByBid(String bid, String userName, HttpServletResponse response) {
        logger.info("通过bid导出终端或调度员崩溃日志===" + bid);
        try {
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/file/handleGetAppBugLogs", JSON.toJSONString(bid));
            List<String> strings = JSON.parseObject(s, new TypeReference<List<String>>() {
            });
            logger.info("导出企业后台调度员或终端日志条数=" + strings.size());

            // 1 直接响应txt文件
//            String fileName = userName + "_BugLog" + ".txt";
//            String content = strings.toString();
//            response.setContentType("text/plain");
//            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            ServletOutputStream outputStream = null;
//            BufferedOutputStream buffer = null;
//            outputStream = response.getOutputStream();
//            response.getOutputStream();
//            buffer = new BufferedOutputStream(outputStream);
//            buffer.write(content.getBytes("UTF-8"));
//            buffer.flush();
//            buffer.close();
//            outputStream.close();

            // 2 响应字符串给前端，前端自己去转成txt文件
            return new RespBean(200, strings.toString());
        } catch (Exception e) {
            return new RespBean(501, "该账号没有崩溃日志记录");
        }
        //无异常则导出文件
//        return null;
    }


    /**
     * 导出日志 方法    todo 暂时未使用
     * 通过流的方式导出文件
     *
     * @param strings  导出的数据
     * @param name     导出的文件名
     * @param response
     */
    @Deprecated
    public void exportBugLog(List<String> strings, String name, HttpServletResponse response) {
        //文件名
        String fileName = name + ".txt";
        //文件内容 字符串类型
        String content = strings.toString();
        //设置响应内容
        response.setContentType("text/plain");
        try {
            //设置响应头 和编码格式
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //通过流输出
        ServletOutputStream outputStream = null;
        BufferedOutputStream buffer = null;
        try {
            outputStream = response.getOutputStream();
//            response.getOutputStream();
            buffer = new BufferedOutputStream(outputStream);
            buffer.write(content.getBytes(StandardCharsets.UTF_8));
            buffer.flush();
            buffer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 远程控制---更新终端连接方式
     *
     * @param option     1 ip更新 2 域名更新
     * @param ip
     * @param port       端口
     * @param domainName 域名
     * @return
     */
    @PostMapping("/handleUpdateClientConnectionMethod")
    public RespBean handleUpdateClientConnectionMethod(Integer option, String ip, String port, String domainName) {
        logger.info("远程控制---更新终端连接方式");
        Map map = new HashMap((int) (4 / 0.75F) + 1);
        if (option == 2) {//域名更新
            map.put("option", "2");
            map.put("domainName", domainName);
            map.put("ip", null);
            map.put("port", null);
        } else {//ip 端口更新
            map.put("option", "1");
            map.put("domainName", null);
            map.put("ip", ip);
            map.put("port", port);
        }
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handleChangeClientConnectionMethod", JSONObject.toJSONString(map));
        if ("false".equals(s)) {
            return new RespBean(505, "切换失败联系管理员");
        }
        return new RespBean(200, "切换成功");
    }


    /**
     * 总后台首页---通过企业id查询终端，项目，调度员 数量统计
     *
     * @param bid
     * @return
     */
    @GetMapping("/handleSelectClientCount")
    public RespBean handleSelectClientCount(String bid) {
        logger.info("总后台首页---查询某企业的终端在线离线数量bid====" + bid);
        if (bid == null || "".equals(bid)) {
            return new RespBean(400, "参数为空");
        }
        //不管bid是对象还是字符串都必须转为json格式才能通过HttpClientUtils工具发送请求
        String json = JSON.toJSONString(bid);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/client/handleSelectClientCount", json);
        //返回的字符串必须转为json对象才能被前端解析
        Object parse = JSONObject.parse(s);
        return new RespBean(ResponseCode.SUCCESS_CODE, parse);
    }


    /**
     * 企业管理 -企业明细- 统计查询
     * Statistic 统计
     *
     * @param address 用户给定的地址范围
     * @param bid     企业bid
     * @return
     */
    @PostMapping("/selectBusinessStatistics")
    public RespBean selectBusinessStatistics(String address, String bid) {
        logger.info("企业管理--企业明细--统计查询=" + address + "===========" + bid);
        Map map = new HashMap((int) (2 / 0.75F) + 1);
        map.put("address", address);
        map.put("token", bid);
        String json = JSON.toJSONString(map);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/client/selectBusinessStatistics", json);
        logger.info("企业后台返回数据============" + s);
        JSONObject jsonObject = JSON.parseObject(s);
        //终端，在线，不在线，总数，调度员在线，不在线，总数，项目在建，完成，总数 共9项数据
        if (jsonObject.size() != 9) {
            return new RespBean(403, "该地址无数据");
        }
        return new RespBean(ResponseCode.SUCCESS_CODE, jsonObject);
    }


}
