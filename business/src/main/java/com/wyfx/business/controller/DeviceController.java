package com.wyfx.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.alarmRange.util.HttpClientUtil;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.DeviceInfo;
import com.wyfx.business.entity.DeviceType;
import com.wyfx.business.entity.vo.DeviceImportExportVo;
import com.wyfx.business.entity.vo.DeviceInfoVo;
import com.wyfx.business.service.DeviceService;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.utils.DateUtil;
import com.wyfx.business.utils.poi.ExcelUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 设备管理
 */
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    @Autowired
    private DeviceService deviceService;
    /**
     * excel导出
     */
    @Autowired
    private IExcelService iExcelService;

    /**
     * 获取启用的设备型号列表
     *
     * @return
     */
    @RequestMapping(value = "/getDeviceType", method = RequestMethod.GET)
    public Object getDeviceType() {
        logger.info("获取启用的设备型号列表");
        List<DeviceType> list = deviceService.findDeviceType();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 添加设备信息
     *
     * @param deviceNum
     * @param type
     * @param IMEI
     * @return
     */
    @RequestMapping(value = "/addDeviceInfo", method = RequestMethod.POST)
//    @RequiresPermissions("device:add")
    @AopLog(describe = "添加设备：", targetParamName = "IMEI", operationType = OperationType.INSERT)
    public Object addDeviceInfo(String deviceNum, Integer type, String IMEI) {
        if (deviceNum == null || deviceNum.equals("") || IMEI == null || IMEI.equals("")) {
            throw new SafetyHatException("设备编号/IMEI不能为空");
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setNumber(deviceNum);
        deviceInfo.setType(type);
        deviceInfo.setImei(IMEI);

        BusinessUser businessUser = getCurrentUser();
        checkNumAndIMEI(deviceNum, IMEI, businessUser);

        deviceInfo.setRecordPerson(businessUser.getUserName());
        deviceInfo.setRecordTime(new Date());
        deviceInfo.setBusinessId(businessUser.getBusinessId());

        deviceService.addDeviceInfo(deviceInfo);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 检查设备编号是否重复{ 如果data:true,则没有重复,否则，已存在该设备编号}
     *
     * @param number
     * @return
     */
    @RequestMapping(value = "/checkDeviceNum", method = RequestMethod.GET)
    public Object checkDeviceNum(String number) {
        BusinessUser businessUser = getCurrentUser();

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), deviceService.checkNumber(businessUser.getBusinessId(), number));
    }

    /**
     * 更新设备信息
     *
     * @param deviceId
     * @param deviceNum
     * @param type
     * @param IMEI
     * @return
     */
    @RequestMapping(value = "/editDeviceInfo", method = RequestMethod.POST)
    @RequiresPermissions("device:edit")
    @AopLog(describe = "编辑设备：", targetParamName = "IMEI", operationType = OperationType.UPDATE)
    public Object editDeviceInfo(Integer deviceId, String deviceNum, Integer type, String IMEI) {
        if (deviceNum == null || deviceNum.equals("") || IMEI == null || IMEI.equals("")) {
            throw new SafetyHatException("设备编号/IMEI不能为空");
        }
        BusinessUser businessUser = getCurrentUser();
        DeviceInfo before = deviceService.findByDetail(deviceId);
        if (!before.getNumber().equals(deviceNum)) {
            checkNumAndIMEI(deviceNum, null, businessUser);
        }
        if (!before.getImei().equals(IMEI)) {
            checkNumAndIMEI(null, IMEI, businessUser);
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDid(deviceId.longValue());
        deviceInfo.setNumber(deviceNum);
        deviceInfo.setType(type);
        deviceInfo.setImei(IMEI);
        deviceInfo.setUpdatePerson(businessUser.getUserName());
        deviceInfo.setUpdateTime(new Date());

        deviceService.updateDeviceInfo(deviceId, deviceInfo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    private boolean checkNumAndIMEI(String deviceNum, String IMEI, BusinessUser businessUser) {
        if (deviceNum != null && deviceNum.length() > 20) {
            throw new SafetyHatException("设备编号过长");
        }
        if (IMEI != null && !(IMEI.length() >= 15 && IMEI.length() <= 17)) {
            throw new SafetyHatException("请检查设备编号/IMEI长度");
        }
        if (deviceNum != null && !deviceService.checkNumber(businessUser.getBusinessId(), deviceNum)) {
            throw new SafetyHatException("设备编号重复");
        }
        if (IMEI != null && !deviceService.checkIMEI(businessUser.getBusinessId(), IMEI)) {
            throw new SafetyHatException("IEMI重复");
        }
        return true;
    }

    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/deleteDevice", method = RequestMethod.GET)
    @RequiresPermissions("device:delete")
    public Object deleteDevice(Integer deviceId) {
        deviceService.deleteDeviceInfo(deviceId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/deleteDevices", method = RequestMethod.GET)
    @RequiresPermissions("device:delete")
    @AopLog(describe = "删除设备：", targetParamName = "imei", operationType = OperationType.DELETE)
    public Object deleteDevice(String deviceIds, @RequestParam(required = false) String imei) {
        deviceService.deleteDeviceInfo(deviceIds);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询设备信息
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/findDetailById", method = RequestMethod.GET)
    @RequiresPermissions("device:findDetail")
    public Object findDetailById(Integer deviceId) {
        DeviceInfo deviceInfo = deviceService.findByDetail(deviceId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), deviceInfo);
    }

    /**
     * 显示设备信息列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/findDevices", method = RequestMethod.GET)
    public Object findDevices(Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        System.out.println("登录账号:" + ";sessionId:" + getSession().getId());
        PageInfo pageInfo = deviceService.findDevices(businessUser.getBusinessId().intValue(), pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据设备状态查询
     *
     * @return
     */
    @RequestMapping(value = "/findDevicesByStatus", method = RequestMethod.GET)
    public Object findDevicesByStatus(Integer status, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = deviceService.findDevicesByStatus(businessUser.getBusinessId().intValue(), status, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据设备型号查询
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/findDevicesByType", method = RequestMethod.GET)
    public Object findDevicesByType(Integer type, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = deviceService.findDevicesByType(businessUser.getBusinessId().intValue(), type, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据时间查询
     *
     * @return
     */
    @RequestMapping(value = "/findDevicesByTime", method = RequestMethod.GET)
    public Object findDevicesByTime(String beginTime, String endTime, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = null;
        try {
            Date begin = (beginTime == null || "".equals(beginTime)) ? DateUtil.formatDate(DateUtil.getCurrentDate()) : DateUtil.formatDate(DateUtil.completionDate(beginTime));
            Date end = (endTime == null || "".equals(endTime)) ? new Date() : DateUtil.formatDate(DateUtil.completionDate(endTime));
            pageInfo = deviceService.findDevicesByTime(businessUser.getBusinessId().intValue(), begin, end, pageSize, pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 检索查询
     *
     * @param type  检索类型
     * @param param 查询参数
     * @return
     */
    @RequestMapping(value = "/findDevicesBy", method = RequestMethod.GET)
    @RequiresPermissions("device:view")
    public Object findDevicesBy(Integer type, String param, Integer pageSize, Integer pageNum,
                                @RequestParam(required = false, value = "projectId") Integer projectId) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = null;
        if (type == null) {
            pageInfo = deviceService.findDevicesByStatus(businessUser.getBusinessId().intValue(), null, pageSize, pageNum);
        } else {
            switch (type) {
                case 0:
                    pageInfo = deviceService.findDevicesByNumber(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;
                case 1:
                    pageInfo = deviceService.findDevicesByIMEI(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;
                case 2:
                    pageInfo = deviceService.findDevicesByAccount(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;
                case 3:
                    pageInfo = deviceService.findDevicesByName(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;
                /*case 4:// todo 根据电话查询
                    pageInfo = deviceService.findDevicesByName(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;*/
                default:
                    pageInfo = deviceService.findDevicesByNumber(businessUser.getBusinessId().intValue(), param, pageSize, pageNum);
                    break;
            }
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 导入excel 设备列表
     * create by wsm on 2019-11-26
     *
     * @param file
     * @return
     */
    @PostMapping("/handleImportDeviceList")
    @RequiresPermissions("device:import")
    public Object handleImportDeviceList(@RequestParam("file") MultipartFile file) {
        logger.info("查看文件是否上传成功==" + file);
        //解析excel，如果有title和header行则添加 占几行则填写几行
        List<DeviceImportExportVo> personList = ExcelUtils.importExcel(file, 0, 1, DeviceImportExportVo.class);
        logger.info("导入数据一共【" + personList.size() + "】行");
        // 保存数据库
        for (DeviceImportExportVo deviceImportExportVo : personList) {
            BusinessUser businessUser = getCurrentUser();
            DeviceInfo deviceInfo = new DeviceInfo();
            String imei = deviceImportExportVo.getImei();
            //判断imei长度是否在15-17位
            if (imei.length() < 15 || imei.length() > 17) {
                return new MyResponseEntity(205, "imei长度必须在15-17位 ");
            }
            if (deviceImportExportVo.getNumber().length() > 20) {
                return new MyResponseEntity(206, " 设备编号必须小于等于20");
            }
            //验证设备编号是否重复
            if (!deviceService.checkNumber(businessUser.getBusinessId(), deviceImportExportVo.getNumber())) {
                return new MyResponseEntity(204, "设备编号重复");
            }
            deviceInfo.setImei(deviceImportExportVo.getImei());
            deviceInfo.setNumber(deviceImportExportVo.getNumber());
            //查询总后台是否存在该设备类型 有则导入 无则不导入
            String type = deviceImportExportVo.getType();
            String jsonString = JSON.toJSONString(type);
            //向总后台字典中查询是否存在该设备
            String uuid = HttpClientUtil.doPostJson("http://" + remoteUrl + "/system/handleSelectDeviceType", jsonString);
            //包含返回 uuid 否则null
            logger.info("设备uuid==" + uuid);
            // 不存在则不导入直接返回
            if (uuid == null || "".equals(uuid) || "null".equals(uuid)) {
                return new MyResponseEntity(202, "设备型号不存在");
            }
            //存在则通过返回的uuid查询device_type中设备的id
            DeviceType deviceType = deviceService.findDeviceTypeByUUID(uuid);
            deviceInfo.setType(deviceType.getId().intValue());
            deviceInfo.setRecordTime(new Date());//录入时间
            deviceInfo.setRecordPerson(businessUser.getName());//录入人
            deviceInfo.setSource(1);//来源(0:手动创建;1:导入设备)
            // deviceInfo.setOnlineFlag(1);//在线状态{0:在线;1:离线}
            deviceInfo.setStatus(0);//设备状态{0:空闲中;1:使用中}

            deviceInfo.setBusinessId(getCurrentUser().getBusinessId());
            //  执行添加导入的设备信息
            deviceService.addDeviceInfo(deviceInfo);
        }
        //测试查看导入的设备列表
        for (DeviceImportExportVo deviceImportExportVo : personList) {
            logger.info("导入设备列表===" + deviceImportExportVo);
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 导出excel 设备列表
     *
     * @param dids
     * @return
     */
    @RequestMapping(value = "/handleExportDeviceList", method = RequestMethod.POST)
    @RequiresPermissions("device:export")
    public Object handleExportDeviceList(String dids, HttpServletResponse response) {
        logger.info("导出excel 设备列表dids=" + dids);
        List<Long> list = JSONArray.parseArray(dids).toJavaList(Long.class);
        Long businessId = getCurrentUser().getBusinessId();//2020-1-8增加企业id查询字段
        List<DeviceInfoVo> deviceTypeList = deviceService.exportDeviceList(businessId, list);
        //执行导出模板
        iExcelService.exportExcel(deviceTypeList, DeviceInfoVo.class, "导出设备列表", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }

    /**
     * 下载导出设备模板excel
     *
     * @param response
     * @return
     */
    @GetMapping("/downLoadDemo")
    public Object handleExportDemo(HttpServletResponse response) {
        logger.info("downLoadDemo 下载导出设备模板excel");
        List<DeviceImportExportVo> deviceImportExportVos = new ArrayList<>();
        //执行导出模板
        iExcelService.exportExcel(deviceImportExportVos, DeviceImportExportVo.class, "模板文件", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 总后台----添加字典信息添加到device_type
     *
     * @param jsonMap
     * @return
     */
    @PostMapping("/handleAddDeviceType")
    public String handleAddDeviceType(@RequestBody String jsonMap) {
        logger.info(" 总后台----添加字典信息添加到device_type =jsonMap=============" + jsonMap);
        JSONObject jsonObject = JSON.parseObject(jsonMap);
        String uuid = jsonObject.get("uuid").toString();
        String type = jsonObject.get("type").toString();//字典类型 T1
        String status = jsonObject.get("status").toString();//字典状态 0启用 1禁用
        String dicType = jsonObject.get("dicType").toString();//标识字典所属类型 暂时只有终端字典用0标识
        boolean b = deviceService.addDeviceType(new DeviceType(null, type, uuid, Integer.parseInt(status), Integer.parseInt(dicType)));
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }


    /**
     * 总后台 ---更新device_type
     *
     * @param jsonMap
     * @return
     */
    @PostMapping("/handleUpdateDeviceType")
    public String handleUpdateDeviceType(@RequestBody String jsonMap) {
        logger.info("  总后台 ---更新device_type  jsonMap= update==" + jsonMap);
        JSONObject jsonObject = JSONObject.parseObject(jsonMap);
        String type = jsonObject.get("type").toString();
        String status = jsonObject.get("status").toString();
        String uuid = jsonObject.get("uuid").toString();
        String dicType = jsonObject.get("dicType").toString();
        boolean b = deviceService.updateDeviceTypeByUUID(new DeviceType(null, type, uuid, Integer.parseInt(status), Integer.parseInt(dicType)));
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }


    /**
     * 总后台---禁用和启用设备(字典)
     *
     * @param json flag {0:启用;1:禁用}
     * @return
     */
    @PostMapping("/handleForbidAndRestartDeviceType")
    public String handleForbidAndRestartDeviceType(@RequestBody String json) {
        logger.info("总后台---禁用和启用设备(字典)");
        //获取数据
        JSONObject jsonObject = JSONObject.parseObject(json);
        String flag = jsonObject.get("flag").toString();
        String dicUUID = jsonObject.get("dicUUID").toString();
        boolean b = deviceService.updateDeviceTypeStatus(dicUUID, Integer.parseInt(flag));
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }

}
