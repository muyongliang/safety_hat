package com.wyfx.total.service.impl;

import com.wyfx.total.entity.DefaultSetting;
import com.wyfx.total.exception.InsertDataException;
import com.wyfx.total.exception.UpdateDataException;
import com.wyfx.total.mapper.DefaultSettingMapper;
import com.wyfx.total.service.IDefaultSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IDefaultSettingServiceImpl implements IDefaultSettingService {

    @Autowired
    private DefaultSettingMapper defaultSettingMapper;

    /**
     * 添加默认设置
     *
     * @param defaultSetting
     */
    @Override
    public boolean addDefaultSetting(DefaultSetting defaultSetting) throws InsertDataException {

        Integer row = defaultSettingMapper.insertSelective(defaultSetting);
        if (row < 0) {
            throw new InsertDataException("插入数据异常，联系管理员");
        }
        return true;
    }


    /**
     * 更新系统默认设置
     *
     * @param defaultSetting
     * @return
     */
    @Override
    public boolean updateDefaultSetting(DefaultSetting defaultSetting) throws UpdateDataException {
        //默认只有条设置记录
        DefaultSetting defaultSetting1 = defaultSettingMapper.selectByPrimaryKey(1L);
        if (defaultSetting1 == null) {
            defaultSettingMapper.insertSelective(defaultSetting);
        }
        defaultSetting.setId(1L);
        int i = defaultSettingMapper.updateByPrimaryKeySelective(defaultSetting);
        if (i < 0) {
            throw new UpdateDataException("更新错误，联系管理员");
        }
        return true;
    }

    /**
     * 查询系统默认设置
     *
     * @return
     */
    @Override
    public DefaultSetting findDefaultSetting() {
        return defaultSettingMapper.selectByPrimaryKey(1L);
    }
}
