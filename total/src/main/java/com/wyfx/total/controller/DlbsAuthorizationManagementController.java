package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.wyfx.total.entity.DiySetting;
import com.wyfx.total.entity.Thirdbusinessmanager;
import com.wyfx.total.entity.vo.BusinessInfo;
import com.wyfx.total.service.DlbsAuthorizationManagementService;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: EnterpriseManagementController
 * @Description: 独立部署授权管理
 * @author： 王世明
 * @date 2019-11-2
 */
@RestController
@RequestMapping(value = "/authorizationManagement")
public class DlbsAuthorizationManagementController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DlbsAuthorizationManagementController.class);
    @Autowired
    private DlbsAuthorizationManagementService dlbsAuthorizationManagementService;
    @Autowired
    private ILogInfService iLogInfService;

    /**
     * 功能：添加第三方企业
     *
     * @param thirdbusinessmanager
     * @return
     */
    @PostMapping(value = "/addManagement")
    public Object addManagement(Thirdbusinessmanager thirdbusinessmanager) {

        Map map = new HashMap(3);
        logger.info("添加第三方企业=" + thirdbusinessmanager);
        String bid = getUUID();
        thirdbusinessmanager.setBid(bid);
        thirdbusinessmanager.setPassword("");
        dlbsAuthorizationManagementService.addManagement(thirdbusinessmanager);
        map.put("message", "添加成功");
        // 添加企业日志记录
        iLogInfService.addLogInfRecord("添加第三方企业", 0, thirdbusinessmanager.getBname());
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 查询账号是否重复
     *
     * @param mainAccount
     * @return
     */
    @GetMapping("/handleQueryTBusinessAccount")
    public RespBean handleQueryAccount(String mainAccount) {
        Thirdbusinessmanager tBusinessByMainAccount = dlbsAuthorizationManagementService.findTBusinessByMainAccount(mainAccount);
        if (tBusinessByMainAccount == null) {
            return new RespBean(200, "企业不存在可以添加");
        }
        return new RespBean(203, "主账号冲突");
    }


    /**
     * 按给定条件查询第三方企业  即第三方企业管理查询
     *
     * @param state   合作状态
     * @param address 地址
     * @param startT  开始时间
     * @param endT    结束时间
     * @param bname   企业名
     * @return
     */
    @PostMapping("/getTBusinessManagerList")
    public RespBean handleGetTBusinessManagerList(Integer state, String address, String startT, String endT, String bname, Integer pageNum, Integer pageSize) throws ParseException {
        logger.info("按给定条件查询第三方企业");
        Map map = new HashMap(3);
        Map res = dlbsAuthorizationManagementService.getTBusinessManagerList(
                state,
                "".equals(bname) ? null : bname,
                "".equals(address) ? null : address,
                "".equals(startT) ? null : startT,
                "".equals(endT) ? null : endT,
                pageNum, pageSize
        );
        map.put("messages", "查询成功");
        return new RespBean(ResponseCode.SUCCESS_CODE, res);
    }


    /**
     * 第三方企业详情查看
     *
     * @param bid
     * @return
     */
    @PostMapping("/TBusinessManagerSelect")
    public RespBean handleFindTBusinessManager(String bid) {
        logger.info("第三方企业详情查看");
        Map map = dlbsAuthorizationManagementService.findTBusinessManagerByBid(bid);
        if (map == null) {
            return new RespBean(203, "企业id不存在");
        }
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 第三方企业编辑更新
     *
     * @param thirdbusinessmanager
     * @return
     */
    @PostMapping("/TBusinessManagerUpdate")
    public RespBean handleUpdateTBusinessManager(Thirdbusinessmanager thirdbusinessmanager) {

        logger.info("第三方企业编辑更新" + thirdbusinessmanager);
        Map map = new HashMap(3);
        dlbsAuthorizationManagementService.UpdateTBusinessManger(thirdbusinessmanager);
        map.put("messages", "更新第三方企业信息成功");
        // 添加企业日志记录
        iLogInfService.addLogInfRecord("更新第三方企业", 2, thirdbusinessmanager.getBname());
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 暂停第三方企业合作状态
     *
     * @param bid
     * @return
     */
    @PostMapping("/pauseTBusinessManager")
    public RespBean handlePauseTBusinessManager(String bid) {
        logger.info("暂停第三方企业合作状态");
        dlbsAuthorizationManagementService.pauseTBusinessManager(bid);
        Map map = new HashMap(3);
        map.put("messages", "暂停操作完成");
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 重启第三方企业合作
     *
     * @param thirdbusinessmanager
     * @return
     */
    @PostMapping("/restartTBusinessManager")
    public RespBean handleRestartTBusinessManager(Thirdbusinessmanager thirdbusinessmanager) {
        logger.info("重启第三方企业合作" + thirdbusinessmanager);
        dlbsAuthorizationManagementService.restartTBusinessManager(thirdbusinessmanager);
        Map map = new HashMap(3);
        map.put("msg", "开启成功");
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 查询所有第三方企业 按更新时间降序排列
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/handleQueryAllTBusinessManager")
    public RespBean handleQueryAllTBusinessManager(int pageNum, int pageSize) {
        Map map = dlbsAuthorizationManagementService.findAllTBusinessManager(pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 企业后台----查询第三方企业信息
     * todo 暂时未用到
     *
     * @param token
     * @return
     */
    @Deprecated
    @PostMapping("/handleQueryTBusinessInfo")
    public String handleQueryTBusinessInfo(@RequestBody String token) {
        Map map = dlbsAuthorizationManagementService.findTBusinessManagerByBid(JSON.parse(token).toString());
        Thirdbusinessmanager tBusiness = (Thirdbusinessmanager) map.get("thirdbusinessmanager");
        BusinessInfo businessInfo = new BusinessInfo(null, tBusiness.getBname(), tBusiness.getBid(), tBusiness.getMainAccount(), tBusiness.getDeviceNumLimit()
                , tBusiness.getState(), 1);//0普通企业 1第三方企业
        //返回给企业后台
        String s = JSON.toJSONString(businessInfo);
        return s;
    }


    /**
     * 企业后台---查询第三企业自定义设置
     *
     * @param token
     * @return
     */
    @Deprecated
    @PostMapping("/handleQueryTBusinessDiySetting")
    public String handleQueryTBusinessDiySetting(@RequestBody String token) {
        //返回给企业后台
        DiySetting diySetting = dlbsAuthorizationManagementService.findDiySetting(JSON.parse(token).toString());
        return JSON.toJSONString(diySetting);
    }

    /**
     * 企业后台---- 验证第三方企业合法性
     *
     * @param mac mac地址
     * @return
     */
    @Deprecated
    @PostMapping("/handleVerificationTBusiness")
    public String handleVerificationTBusiness(@RequestBody String mac, HttpServletRequest request) {
//        Map map = JSON.parseObject(msg, Map.class);
//        String mac = map.get("mac").toString();
//        String ip = map.get("ip").toString();

        String ip = request.getServerName();
        Map map1 = dlbsAuthorizationManagementService.selectTBusinessByIpAndMac(ip, JSON.parse(mac).toString());
        BusinessInfo businessInfo = (BusinessInfo) map1.get("businessInfo");
        Map respMap = new HashMap((int) (2 / 0.75F) + 1);
        if (businessInfo == null || "".equals(businessInfo)) {
            respMap.put("status", "false");
        } else {
            respMap.put("status", "true");
            respMap.put("businessInfo", businessInfo);
        }
        return JSON.toJSONString(respMap);
    }


}
