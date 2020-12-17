package com.wyfx.total.service;

import com.wyfx.total.entity.Dictionaries;

import java.util.Map;


/**
 * 字典
 */
public interface IDictionaryService {

    boolean addDictionary(Dictionaries dictionaries);

    boolean updateDictionary(Dictionaries dictionaries);

    /**
     * 禁用和启用字典
     *
     * @param dicId
     * @param flag  {0:已启用;1:禁用}
     * @return
     */
    boolean forbidDictionary(Integer dicId, Integer flag);

    Dictionaries findDicByDicId(Integer dicId);

    Map selectAll(Integer pageNum, Integer pageSize);

    String selectAllDicIfHaveDeviceType(String type);

    /**
     * 移动排序
     *
     * @param dicId
     * @param step
     * @param orderNum
     * @return
     */
    boolean moveOrderNum(Integer dicId, Integer step, Integer orderNum);

}
