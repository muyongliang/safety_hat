package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.DeviceInfo;
import com.wyfx.business.entity.DeviceType;
import com.wyfx.business.entity.vo.DeviceInfoVo;

import java.util.Date;
import java.util.List;

public interface DeviceService {

    boolean checkNumber(Long businessId, String number);

    boolean checkIMEI(Long businessId, String IMEI);

    /**
     * 添加设备
     *
     * @param deviceInfo
     */
    void addDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 更新设备信息
     *
     * @param deviceId
     * @param deviceInfo
     */
    void updateDeviceInfo(Integer deviceId, DeviceInfo deviceInfo);

    /**
     * 导入设备
     *
     * @param deviceInfo
     */
    void importDevice(DeviceInfo deviceInfo);

    /**
     * 导出设备
     *
     * @param businessId
     */
    void exportDevice(Integer businessId);

    /**
     * 删除设备
     *
     * @param deviceId
     */
    void deleteDeviceInfo(Integer deviceId);

    void deleteDeviceInfo(String deviceIds);

    /**
     * 查询设备详细信息
     *
     * @param deviceId
     * @return
     */
    DeviceInfo findByDetail(Integer deviceId);

    DeviceInfo findDetailByBid(Long bid);

    /**
     * 查询企业的设备列表
     *
     * @param businessId
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfo findDevices(Integer businessId, int pageSize, int pageNum);

    /**
     * 根据状态查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByStatus(Integer businessId, Integer status, int pageSize, int pageNum);

    /**
     * 根据设备型号查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByType(Integer businessId, Integer type, int pageSize, int pageNum);

    /**
     * 根据设备录入时间查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByTime(Integer businessId, Date beginTime, Date endTime, int pageSize, int pageNum);

    /**
     * 根据设备编号查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByNumber(Integer businessId, String number, int pageSize, int pageNum);

    /**
     * 根据设备IMEI查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByIMEI(Integer businessId, String imei, int pageSize, int pageNum);

    /**
     * 根据终端账号查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByAccount(Integer businessId, String account, int pageSize, int pageNum);

    /**
     * 根据终端姓名查询
     *
     * @param businessId
     * @return
     */
    PageInfo findDevicesByName(Integer businessId, String name, int pageSize, int pageNum);

    List<DeviceType> findDeviceType();

    /**
     * 导出设备列表
     *
     * @param ids
     */
    List<DeviceInfoVo> exportDeviceList(Long businessId, List<Long> ids);


    /**
     * 添加设备型号数据获取自增主键
     *
     * @param deviceType
     * @return
     */
    boolean addDeviceType(DeviceType deviceType);

    /**
     * 通过唯一uuid更新
     *
     * @param deviceType
     * @return
     */
    boolean updateDeviceTypeByUUID(DeviceType deviceType);

    DeviceType findDeviceTypeByUUID(String uuid);

    /**
     * 更新设备启用禁用状态
     *
     * @param uuid flag{0:启用;1:禁用}
     * @return
     */
    boolean updateDeviceTypeStatus(String uuid, Integer flag);


}
