package com.wyfx.business.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.ClientAccountMapper;
import com.wyfx.business.dao.DeviceInfoMapper;
import com.wyfx.business.dao.DeviceTypeMapper;
import com.wyfx.business.entity.DeviceInfo;
import com.wyfx.business.entity.DeviceType;
import com.wyfx.business.entity.vo.DeviceInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 设备业务类
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;
    @Autowired
    private ClientAccountMapper clientAccountMapper;

    @Override
    public boolean checkNumber(Long businessId, String number) {
        DeviceInfo deviceInfo = deviceInfoMapper.checkDeviceNumber(businessId.intValue(), number);
        boolean flag = deviceInfo == null;
        return flag;
    }

    @Override
    public boolean checkIMEI(Long businessId, String IMEI) {
        DeviceInfo deviceInfo = deviceInfoMapper.checkDeviceIMEI(businessId.intValue(), IMEI);
        boolean flag = deviceInfo == null;
        return flag;
    }

    /**
     * 添加设备
     *
     * @param deviceInfo
     */
    @Override
    @Transactional
    public void addDeviceInfo(DeviceInfo deviceInfo) {
        deviceInfoMapper.insertSelective(deviceInfo);
    }

    /**
     * 更新设备信息
     *
     * @param deviceId
     * @param deviceInfo
     */
    @Override
    @Transactional
    public void updateDeviceInfo(Integer deviceId, DeviceInfo deviceInfo) {
        deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
    }

    /**
     * 导入设备
     *
     * @param deviceInfo
     */
    @Override
    public void importDevice(DeviceInfo deviceInfo) {

    }

    /**
     * 导出设备
     *
     * @param businessId
     */
    @Override
    public void exportDevice(Integer businessId) {

    }

    /**
     * 删除设备,将设备状态更改为删除锁定状态，而不是删除设备数据
     *
     * @param deviceId
     */
    @Override
    @Transactional
    public void deleteDeviceInfo(Integer deviceId) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDid(deviceId.longValue());
        deviceInfo.setStatus(-1);
        deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
    }

    @Override
    public void deleteDeviceInfo(String deviceIds) {
        List<Long> list = Arrays.stream(deviceIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        for (Long id : list) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDid(id.longValue());
            deviceInfo.setStatus(-1);
            deviceInfoMapper.updateByPrimaryKeySelective(deviceInfo);
        }
    }

    /**
     * 查询设备详细信息
     *
     * @param deviceId
     * @return
     */
    @Override
    public DeviceInfo findByDetail(Integer deviceId) {
        return deviceInfoMapper.selectByPrimaryKey(deviceId.longValue());
    }

    @Override
    public DeviceInfo findDetailByBid(Long bid) {
        return deviceInfoMapper.selectDetailByBid(bid);
    }

    /**
     * 查询企业的设备列表
     *
     * @param businessId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevices(Integer businessId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectByBusinessId(businessId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据状态查询
     *
     * @param businessId
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByStatus(Integer businessId, Integer status, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = (status == null) ? deviceInfoMapper.selectDevicesByStatus(businessId, null) : deviceInfoMapper.selectDevicesByStatus(businessId, status);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据设备型号查询
     *
     * @param businessId
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByType(Integer businessId, Integer type, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByType(businessId, type);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据设备录入时间查询
     *
     * @param businessId
     * @param beginTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByTime(Integer businessId, Date beginTime, Date endTime, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByTime(businessId, beginTime, endTime);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据设备编号查询
     *
     * @param businessId
     * @param number
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByNumber(Integer businessId, String number, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByNumber(businessId, number);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据设备IMEI查询
     *
     * @param businessId
     * @param imei
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByIMEI(Integer businessId, String imei, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByIMEI(businessId, imei);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据终端账号查询
     *
     * @param businessId
     * @param account
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByAccount(Integer businessId, String account, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByAccount(businessId, account);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据终端姓名查询
     *
     * @param businessId
     * @param name
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findDevicesByName(Integer businessId, String name, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DeviceInfoVo> list = deviceInfoMapper.selectDevicesByName(businessId, name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 查询设备型号:调用总后台的查询接口
     *
     * @return
     */
    @Override
    public List<DeviceType> findDeviceType() {
        List<DeviceType> list = deviceTypeMapper.selectAll();
        List<DeviceType> listA = new ArrayList<>();
        DeviceType deviceType = new DeviceType(0L, "全部", null, 0, 0);
        listA.add(deviceType);
        for (int i = 0; list != null && i < list.size(); i++) {
            listA.add(list.get(i));
        }
        return listA;
    }

    /**
     * 导出设备列表
     * 修改
     *
     * @param ids 设备id
     */
    @Override
    public List<DeviceInfoVo> exportDeviceList(Long businessId, List<Long> ids) {
//        //查询终端设备信息
//        List<DeviceInfo> deviceTypeList = deviceInfoMapper.selectByIds(ids);
//        //查询终端账号信息
//        List<ClientAccount> clientAccountList = clientAccountMapper.selectByDeviceIds(ids);
//        //封装需要返回给前端的数据
//        List<DeviceInfoVo> list = new ArrayList<>();
//        for (int i = 0, count = deviceTypeList.size(); i < count; i++) {
//            DeviceInfoVo deviceInfoVo = new DeviceInfoVo(
//                    null,
//                    deviceTypeList.get(i).getNumber(),
//                    deviceTypeList.get(i).getType().toString(),
//                    deviceTypeList.get(i).getImei(),
//                    deviceTypeList.get(i).getStatus(),
//                    deviceTypeList.get(i).getRecordTime(),
//                    clientAccountList.get(i).getAccount(),
//                    clientAccountList.get(i).getName()
//            );
//            list.add(deviceInfoVo);
//        }
        List<DeviceInfoVo> deviceInfoVos = deviceInfoMapper.selectByDids(businessId.intValue(), ids);
        System.out.println("导出设备列表=" + deviceInfoVos);
        return deviceInfoVos;
    }

    /**
     * 添加设备类型数据 获取自增主键
     *
     * @param deviceType
     * @return
     */
    @Override
    public boolean addDeviceType(DeviceType deviceType) {
        int i = deviceTypeMapper.insertSelective(deviceType);
        return i >= 0;
    }


    /**
     * 通过唯一uuid更新
     *
     * @param deviceType
     * @return
     */
    @Override
    public boolean updateDeviceTypeByUUID(DeviceType deviceType) {
        int i = deviceTypeMapper.updateByUUIDSelective(deviceType);
        return i >= 0;
    }

    /**
     * 通过uuid查询设备类型
     *
     * @param uuid
     * @return
     */
    @Override
    public DeviceType findDeviceTypeByUUID(String uuid) {
        DeviceType deviceType = deviceTypeMapper.selectByUUID(uuid);
        System.out.println("type===" + deviceType);
        return deviceType;
    }

    /**
     * 总后台禁用启用设备
     * flag {0:启用;1:禁用}
     *
     * @param uuid
     * @return
     */
    @Override
    public boolean updateDeviceTypeStatus(String uuid, Integer flag) {
        DeviceType deviceType = new DeviceType();
        deviceType.setUuid(uuid);
        deviceType.setStatus(flag);
        int i = deviceTypeMapper.updateByUUIDSelective(deviceType);
        return i >= 0;
    }
}
