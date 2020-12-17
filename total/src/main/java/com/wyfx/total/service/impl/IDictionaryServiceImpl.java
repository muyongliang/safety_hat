package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.Dictionaries;
import com.wyfx.total.exception.CanNotMaveException;
import com.wyfx.total.exception.DictionariesNameConflictException;
import com.wyfx.total.mapper.DictionariesMapper;
import com.wyfx.total.service.IDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IDictionaryServiceImpl implements IDictionaryService {

    private static final Logger logger = LoggerFactory.getLogger(IDictionaryServiceImpl.class);
    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Override
    public boolean addDictionary(Dictionaries dictionaries) throws DictionariesNameConflictException {
        logger.info("添加字典===" + dictionaries);
        //判断字典是否重复
        Dictionaries dictionaries1 = dictionariesMapper.selectByOptionName(dictionaries.getOptionName());
        if (dictionaries1 != null) {
            throw new DictionariesNameConflictException("字典名重复");
        }

        // todo  start 实现排序
        //查询目前的最大排序号即最大的企业数
        int maxEnableNum = dictionariesMapper.selectMaxEnableNum();
        //设置新的排序数
        int newMaxEnableNum = maxEnableNum + 1;
        //判断字典的启用状况 {0:已启用;1:禁用}
        if (dictionaries.getFlag() == 1) {
            dictionaries.setOrderNum(null);
        } else {
            dictionaries.setOrderNum(newMaxEnableNum);
        }
        //todo end

        int i = dictionariesMapper.insertSelective(dictionaries);
        return i >= 0;
    }

    /**
     * 编辑字典
     *
     * @param dictionaries
     * @return
     */
    @Override
    public boolean updateDictionary(Dictionaries dictionaries) throws DictionariesNameConflictException {
        logger.info("编辑字典" + dictionaries);
        //判断字典是否重复
        Dictionaries dictionaries1 = dictionariesMapper.selectByOptionName(dictionaries.getOptionName());
        if (dictionaries1 != null) {
            throw new DictionariesNameConflictException("字典名重复");
        }
        // todo  start 实现排序
        //查询目前的最大排序号即最大的企业数
        int maxEnableNum = dictionariesMapper.selectMaxEnableNum();
        //设置新的排序数
        int newMaxEnableNum = maxEnableNum + 1;
        //判断字典的启用状况 {0:已启用;1:禁用}
        if (dictionaries.getFlag() == 1) {
            dictionaries.setOrderNum(null);
        } else {
            dictionaries.setOrderNum(newMaxEnableNum);
        }
        //todo end
        int i = dictionariesMapper.updateByPrimaryKeySelective(dictionaries);
        return i >= 0;
    }

    /**
     * 禁用启用字典
     *
     * @param dicId flag {0:启用;1:禁用}
     * @return
     */
    @Override
    public boolean forbidDictionary(Integer dicId, Integer flag) {
        logger.info("禁用启用字典的id" + dicId);
        Dictionaries dictionaries = dictionariesMapper.selectByPrimaryKey(dicId);
        dictionaries.setUpdateTime(new Date());
        //状态标记{0:启用;1:禁用}
        if (flag == 0) {
            //设置字典为启用
            dictionaries.setFlag(flag);
            //启用排序
            //查询目前的最大排序号即最大的企业数
            int maxEnableNum = dictionariesMapper.selectMaxEnableNum();
            //设置新的排序数
            int newMaxEnableNum = maxEnableNum + 1;
            //设置排序数
            dictionaries.setOrderNum(newMaxEnableNum);
        } else {
            //设置字典为禁用
            dictionaries.setFlag(flag);
            //更新所有的排序
            boolean b = updateAllOrder(dictionaries);
            if (!b) {
                return false;
            }
            //去除排序字段
            dictionaries.setOrderNum(null);
        }
        int i = dictionariesMapper.updateByPrimaryKey(dictionaries);
        return i >= 0;
    }

    /**
     * 更新所有的排序
     *
     * @return
     */
    public boolean updateAllOrder(Dictionaries dictionaries) {
        //获取本字典的排序数
        Integer orderNum = dictionaries.getOrderNum();
        //查询所以排序大于本字典排序的字典集合
        List<Integer> integers = dictionariesMapper.selectAllOrderNumLargerThanMe(orderNum);
        //没有数据则不操作更新排序字段
        if (integers.size() == 0) {
            return true;
        }
        //修改所有的排序号减一
        int b = dictionariesMapper.updateAllOrderNum(integers);
        if (b < 1) {
            logger.error("更新数据失败");
            return false;
        }
        return true;
    }


    /**
     * 查询所有字典信息
     *
     * @return
     */
    @Override
    public Map selectAll(Integer pageNum, Integer pageSize) {
        logger.info("查询所有字典信息");
        PageHelper.startPage(pageNum, pageSize);
        List<Map> dictionaries = dictionariesMapper.selectAllOrderByFlag();
        //最大启用数 即最大可排序字典数
        int maxEnableNum = dictionariesMapper.selectMaxEnableNum();
        PageInfo pageInfo = new PageInfo(dictionaries);
        Map respMap = new HashMap((int) (3 / 0.75F + 1));
        respMap.put("dicList", pageInfo);
        respMap.put("count", pageInfo.getTotal());
        respMap.put("max", maxEnableNum);
        return respMap;
    }

    /**
     * 查询某条字典的详情
     *
     * @param dicId
     * @return
     */
    @Override
    public Dictionaries findDicByDicId(Integer dicId) {
        logger.info("查询某条字典的详情" + dicId);
        Dictionaries dictionaries = dictionariesMapper.selectByPrimaryKey(dicId);
        return dictionaries;
    }


    /**
     * 查询字典中是否包含指定设备类型
     *
     * @return 包含返回 uuid 否则null
     */
    @Override
    public String selectAllDicIfHaveDeviceType(String type) {
        logger.info("查询字典中是否包含指定设备类型并返回字典的uuid" + type);
        List<Map> mapList = dictionariesMapper.selectAllOrderByFlag();
        List<Map> option = new ArrayList<>();
        for (Map map : mapList) {
            if (map.get("option_name").equals(type)) {
                option.add(map);
            }
        }
        if (option.size() == 0) {
            return null;
        }
        String uuid = option.get(0).get("uuid").toString();
        logger.info("返回的uuid===" + uuid);
        return uuid;
    }

    /**
     * 移动排序
     *
     * @param dicId    暂时没有用此字段
     * @param step
     * @param orderNum
     * @return
     */
    @Override
    public boolean moveOrderNum(Integer dicId, Integer step, Integer orderNum) throws CanNotMaveException {
        //通过排序查询字典 移动的字典
        Dictionaries dictionaries = dictionariesMapper.selectByOrderNum(orderNum);
        if (dictionaries == null) {
            throw new CanNotMaveException("被禁用的字典不能排序");
        }
        logger.info("移动的字典=" + dictionaries);
        //查询被动改变排序的字典
        Dictionaries dictionaries1 = dictionariesMapper.selectByOrderNum(orderNum + step);
        logger.info("被动改变排序的字典" + dictionaries1);
        //查询最大排序字段
        int maxEnableNum = dictionariesMapper.selectMaxEnableNum();
        //更新排序字段 如果排序字段为1不能上移 如果排序字段为最大值不能下移
        if (orderNum == 1 && step == -1 || orderNum == maxEnableNum && step == 1) {
            throw new CanNotMaveException("不能这样移动");
        }
        //更新移动字典
        dictionaries.setOrderNum(orderNum + step);
        int i = dictionariesMapper.updateByPrimaryKeySelective(dictionaries);
        //更新被动改变的字典
        dictionaries1.setOrderNum(orderNum);
        int i1 = dictionariesMapper.updateByPrimaryKeySelective(dictionaries1);
        if (i < 1 || i1 < 1) {
            logger.error("更新字典排序数据失败");
            return false;
        }
        return true;
    }
}
