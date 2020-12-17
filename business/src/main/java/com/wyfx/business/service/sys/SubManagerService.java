package com.wyfx.business.service.sys;

import com.wyfx.business.entity.vo.SubManagerVo;

import java.util.Map;

/**
 * create by wsm on2019-1-29
 */
public interface SubManagerService {
    /**
     * 通过状态查询 子账号列表
     *
     * @param state
     * @return
     */
    Map selectSubManagerByState(Integer state, Long businessId, Integer pageNum, Integer pageSize);


    Map selectSubManagerById(Integer bid);

    boolean editSubManager(SubManagerVo subManagerVo, String roleList);

    /**
     * 重置密码
     *
     * @param subManagerVo
     * @return
     */
    boolean resetPwd(SubManagerVo subManagerVo);

    /**
     * 给变子账号状态 禁用 启用
     *
     * @param subManagerVo
     * @return
     */
    boolean updateStatus(SubManagerVo subManagerVo);

    /**
     * 通过账号 姓名 电话检索
     *
     * @param subManagerVo
     * @return
     */
    Map selectByCondition(SubManagerVo subManagerVo, Long businessId, Integer pageNum, Integer pageSize);


}
