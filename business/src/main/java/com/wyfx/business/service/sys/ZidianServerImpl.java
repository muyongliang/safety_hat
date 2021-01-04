package com.wyfx.business.service.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.ZidianSettingMapper;
import com.wyfx.business.entity.ZidianSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/6
 * @description 字典业务类
 */
@Service
@Transactional
public class ZidianServerImpl implements ZidianServer {

    @Autowired
    private ZidianSettingMapper zidianSettingMapper;

    /**
     * 添加工种
     *
     * @param zidianSetting
     */
    @Override
    @Transactional
    public void addWorkType(ZidianSetting zidianSetting) {
        //查询排序字段的最大值,并在其基础上加1
        Integer maxOrderNum = zidianSettingMapper.findMaxOrderNum(zidianSetting.getBusinessId());
        int orderNum = (maxOrderNum == null) ? 1 : ++maxOrderNum;
        zidianSetting.setOrderNum(orderNum);
        zidianSetting.setUpdateTime(new Date());
        zidianSettingMapper.insertSelective(zidianSetting);
    }

    /**
     * 更新工种启用状态
     *
     * @param zid
     * @param status
     */
    @Override
    @Transactional
    public void updateWorkTypeStatus(int zid, int status, Integer businessId) {
        if (status == 1) {
            //禁用
            zidianSettingMapper.updateStatusAndOrderNum(zid, status);
            return;
        }
        //启用
        Integer maxOrderNum = zidianSettingMapper.findMaxOrderNum(businessId.longValue());
        maxOrderNum = maxOrderNum + 1;
        zidianSettingMapper.updateWorkTypeStatus(zid, status, maxOrderNum);
    }

    /**
     * 更新工种信息
     *
     * @param zidianSetting
     */
    @Override
    @Transactional
    public void updateWorkType(ZidianSetting zidianSetting) {
        zidianSettingMapper.updateByPrimaryKeySelective(zidianSetting);
    }

    /**
     * 查询该工种下所有工种信息
     *
     * @param pageSize   页面大小
     * @param pageNum    当前页码
     * @param zidianType 字典类型
     * @return
     */
    @Override
    public PageInfo<ZidianSetting> findAll(Integer pageSize, Integer pageNum, int zidianType, Long businessId) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ZidianSetting> list = zidianSettingMapper.findPageByWorkType(zidianType, businessId, null);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 改变字典排序
     *
     * @param zid      字典ID
     * @param orderNum 字典排序字段r
     * @param moveType 移动类型{0:升序(增加排序);1:降序(减少排序)}
     * @return
     */
    @Override
    public void changeZidianOrderNum(Integer zid, Integer orderNum, Integer moveType, Long businessId) throws Exception {
        Integer maxOrderNum = zidianSettingMapper.findMaxOrderNum(businessId);

        if ((moveType == 0 && orderNum >= maxOrderNum) || (moveType == 1 && orderNum <= 1)) {
            throw new Exception("已经在头部/尾部,无法对选中字典进行重新排序");
        }

        ZidianSetting zidianSetting = new ZidianSetting();
        Integer changedNum = new Integer(orderNum);
        changedNum = (moveType == 0) ? ++changedNum : --changedNum;
        zidianSetting.setZid(zid.longValue());
        zidianSetting.setOrderNum(changedNum);
        //更新当前工种的排序
        zidianSettingMapper.updateByPrimaryKeySelective(zidianSetting);
        //更新原排序的工种排序
        zidianSettingMapper.updateByOrderNum(zid.longValue(), changedNum, orderNum);
    }

    /**
     * 查询当前最大的排序字段
     *
     * @param businessId
     * @return
     */
    @Override
    public Integer findMaxOrderNum(Long businessId) {
        return zidianSettingMapper.findMaxOrderNum(businessId);
    }

    /**
     * 获取工种列表
     *
     * @param zidianType
     * @return
     */
    @Override
    public List<ZidianSetting> getWorkTypeList(int zidianType, Long businessId) {
        return zidianSettingMapper.findPageByWorkType(zidianType, businessId, 1L);
    }

    @Override
    public ZidianSetting findByName(Long businessId, String name) {
        return zidianSettingMapper.selectByName(businessId, name);
    }
}
