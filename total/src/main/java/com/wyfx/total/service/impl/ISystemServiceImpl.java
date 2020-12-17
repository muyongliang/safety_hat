package com.wyfx.total.service.impl;

import com.wyfx.total.entity.PageSetting;
import com.wyfx.total.exception.NotHavePageSettingDataException;
import com.wyfx.total.mapper.PageSettingMapper;
import com.wyfx.total.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ISystemServiceImpl implements ISystemService {

    @Autowired
    private PageSettingMapper pageSettingMapper;

    /**
     * 添加更新系统设置 --页面设置
     *
     * @param pageSetting
     * @return
     */
    @Override
    @Transactional
    public boolean updatePageSetting(PageSetting pageSetting) {
        PageSetting page = pageSettingMapper.selectByPrimaryKey(1);//企业总后台只有一条页面设置记录
        if (page == null) {
            pageSettingMapper.insertSelective(pageSetting);
        } else {
            pageSetting.setPid(page.getPid());
            pageSettingMapper.updateByPrimaryKeySelective(pageSetting);
        }
        return true;
    }

    /**
     * 系统设置 ----查询页面设置
     * 默认只有一条数据
     *
     * @return
     */
    @Override
    public Map selectPageSetting() throws NotHavePageSettingDataException {
        PageSetting pageSetting = pageSettingMapper.selectByPrimaryKey(1);
        if (pageSetting == null) {
            throw new NotHavePageSettingDataException("请先添加页面设置");
        }
        Map map = new HashMap();
        map.put("metaUrl", pageSetting.getMetaUrl());
        map.put("logoUrl", pageSetting.getLogoUrl());
        map.put("systemName", pageSetting.getSystemName());
        return map;
    }
}
