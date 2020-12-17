package com.wyfx.business.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.BusinessUserMapper;
import com.wyfx.business.dao.UserRoleMapper;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.UserRole;
import com.wyfx.business.entity.vo.SubManagerVo;
import com.wyfx.business.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子账号管理 业务层
 * crete by wsm on 2019-11-29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SubManagerServiceImpl implements SubManagerService {


    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Map selectSubManagerByState(Integer state, Long businessId, Integer pageNum, Integer pageSize) {

        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        List<SubManagerVo> subManagerList = businessUserMapper.findSubManagerByStatus(state, businessId);
        System.out.println(subManagerList);
        PageInfo pageInfo = new PageInfo(subManagerList);
        Map map = new HashMap();
        map.put("subManager", pageInfo);
        map.put("count", businessUserMapper.findSubManagerByStatus(null, businessId).size());
        return map;
    }


    /**
     * 通过id查询子账号
     *
     * @param bid
     * @return
     */
    @Override
    public Map selectSubManagerById(Integer bid) {
        List<SubManagerVo> subManagerVoList = businessUserMapper.findSubManagerById(bid);
        Map resMap = new HashMap();
        resMap.put("subManager", subManagerVoList);
        System.out.println("==================sub" + subManagerVoList);
        return resMap;
    }

    /**
     * 编辑
     *
     * @param subManagerVo
     * @return
     */
    @Override
    @Transactional
    public boolean editSubManager(SubManagerVo subManagerVo, String roleList) {
        //通过id查询子账号信息
        BusinessUser businessUser = businessUserMapper.selectByPrimaryKey(subManagerVo.getBid());
        //更新
        businessUser.setName(subManagerVo.getName());
        businessUser.setUserName(subManagerVo.getUsername());
        businessUser.setMobile(subManagerVo.getMobile());
        businessUserMapper.updateByPrimaryKeySelective(businessUser);
        //更新用户角色表
        JSONObject jsonObject = JSONObject.parseObject(roleList);
        //角色列表
        Object[] members = jsonObject.getJSONArray("role").toArray();
        System.out.println("角色列表====" + Arrays.toString(members));
        //更新用户角色关系表 先删除再添加
        int b = 0;
        if (members != null && members.length >= 0) {
            b = userRoleMapper.deleteByPrimaryKey(subManagerVo.getBid());
        }
        if (b < 0) {
            return false;
        }
        for (int i = 0; i < members.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setUserId(subManagerVo.getBid());
            userRole.setRoleId(((Integer) members[i]).longValue());
            System.out.println(userRole);
            int insert = userRoleMapper.insert(userRole);
            if (insert < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重置密码
     *
     * @param subManagerVo
     * @return
     */
    @Override
    @Transactional
    public boolean resetPwd(SubManagerVo subManagerVo) {
        BusinessUser businessUser = businessUserMapper.selectByPrimaryKey(subManagerVo.getBid());
        //通过过用户名和密码加密后存入数据库  分屏子账号的用户名和密码一致
        businessUser.setPassword(MD5Util.encrypt(businessUser.getUserName(), businessUser.getUserName()));
        int i = businessUserMapper.updateByPrimaryKeySelective(businessUser);
        return i >= 0;
    }


    /**
     * 改变子账号 启用 禁用状态
     *
     * @param subManagerVo
     * @return
     */
    @Override
    @Transactional
    public boolean updateStatus(SubManagerVo subManagerVo) {
        BusinessUser businessUser = businessUserMapper.selectByPrimaryKey(subManagerVo.getBid());
        businessUser.setStatus(subManagerVo.getStatus());
        int i = businessUserMapper.updateByPrimaryKeySelective(businessUser);
        return i >= 0;
    }

    /**
     * 通过账号 姓名 电话 检索
     *
     * @param subManagerVo
     * @return
     */
    @Override
    public Map selectByCondition(SubManagerVo subManagerVo, Long businessId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<SubManagerVo> byCondition = businessUserMapper.findByCondition(
                subManagerVo.getUsername() == null || "".equals(subManagerVo.getUsername()) ? null : subManagerVo.getUsername(),
                subManagerVo.getName() == null || "".equals(subManagerVo.getName()) ? null : subManagerVo.getName(),
                subManagerVo.getMobile() == null || "".equals(subManagerVo.getMobile()) ? null : subManagerVo.getMobile()
        );
        PageInfo pageInfo = new PageInfo(byCondition);
        Map respMap = new HashMap();
        respMap.put("subManager", pageInfo);
        respMap.put("count", businessUserMapper.findSubManagerByStatus(null, businessId).size());
        return respMap;
    }
}
