package com.wyfx.business.service.shiro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.UserRealm;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.dao.*;
import com.wyfx.business.entity.*;
import com.wyfx.business.entity.vo.DispatcherVo;
import com.wyfx.business.entity.vo.MyDispatcherVo;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.UUIDUtil;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 用户管理
 */
@Service
@Transactional
public class BusinessUserServiceImpl implements BusinessUserService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessUserServiceImpl.class);
    @Autowired
    private BusinessUserMapper businessUserMapper;
    @Autowired
    private BusinessAreasMapper businessAreasMapper;
    @Autowired
    private ClientAccountMapper clientAccountMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRealm userRealm;
    @Autowired
    private TalkBackGroupMemberMapper talkBackGroupMemberMapper;
    @Autowired
    private TalkBackGroupMapper talkBackGroupMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleBusinessMapper roleBusinessMapper;
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;
    @Autowired
    private IExcelService iExcelService;

    /**
     * 更新用户的在线状态
     *
     * @param userName
     * @param status
     */
    @Override
    public void updateOnlineStatus(String userName, int status) {
        businessUserMapper.updateOnlineStatus(userName, status);
    }

    @Override
    public void updateNameOrPwd(Long bid, String name, String pwd) {
        businessUserMapper.updateNameOrPwd(bid, name, pwd);
        userRealm.clearCache();
    }

    /**
     * 移动账号到指定项目
     *
     * @param projectId
     * @param clientIds
     */
    @Override
    public void moveToTargetProject(Integer projectId, Long[] clientIds, BusinessUser currentUser) throws Exception {

        for (int i = 0; i < clientIds.length; i++) {
            BusinessUser businessUser = new BusinessUser();
            businessUser.setBid(clientIds[i]);
            businessUser.setProjectId(projectId.longValue());
            businessUser.setModifyPerson(currentUser.getUserName());
            businessUser.setModifyTime(new Date());
            addBusinessUser(businessUser);
            //查询指定项目的默认对讲组
            TalkBackGroup talkBackGroup = talkBackGroupMapper.selectByNameAndProjectId(projectId.longValue(), "默认分组");
            talkBackGroupMemberMapper.deleteByBid(clientIds[i]);
            talkBackGroupMemberMapper.insert(new TalkBackGroupMember(clientIds[i], talkBackGroup.getId()));
        }
    }

    @Override
    public boolean checkAccount(String userName, Long businessId) {
        try {
            BusinessUser businessUser = businessUserMapper.findByBusinessIdAndUserName(businessId.intValue(), userName);
            boolean flag = businessUser == null;
            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void addUser(String username, String password) {
        BusinessUser user = new BusinessUser();
        user.setUserName(username);
        user.setPassword(MD5Util.encrypt(username, password));
        user.setToken(UUIDUtil.getUUID());
        user.setCreateTime(new Date());
        businessUserMapper.insertSelective(user);
    }

    /**
     * 添加/更新用户
     *
     * @param businessUser
     */
    @Override
    public void addBusinessUser(BusinessUser businessUser) throws Exception {
        if (businessUser.getBid() == null) {
            if (businessUserMapper.findByUserName(businessUser.getUserName()) != null) {
                throw new SafetyHatException("账号已存在");
            }
            Integer userType = businessUser.getUserType();
            if (userType != 2) {
                businessUser.setZidianId(0L);
            }
            businessUserMapper.insertSelective(businessUser);

            // 关联用户角色(调度员/终端/管理员)
            if (userType == 0) {//添加企业管理员时，将默认角色和企业关联
                roleBusinessMapper.insert(new RoleBusiness(businessUser.getBusinessId(), 1L));
                roleBusinessMapper.insert(new RoleBusiness(businessUser.getBusinessId(), 2L));
                roleBusinessMapper.insert(new RoleBusiness(businessUser.getBusinessId(), 3L));
                roleBusinessMapper.insert(new RoleBusiness(businessUser.getBusinessId(), 4L));
            }
            if (userType != null && userType != UserTypeAndStatus.OTHER_MANAGER) {
                userType = userType + 1;
                userRoleMapper.insert(new UserRole(businessUser.getBid(), userType.longValue()));
            }
            //添加对讲组成员:默认分组
            if (businessUser.getProjectId() != null && businessUser.getProjectId() > 0 && (userType == 1 || userType == 2)) {
                TalkBackGroup talkBackGroup = talkBackGroupMapper.selectByNameAndProjectId(businessUser.getProjectId(), "默认分组");
                TalkBackGroupMember talkBackGroupMember = new TalkBackGroupMember(businessUser.getBid(), talkBackGroup.getId());
                talkBackGroupMemberMapper.insert(talkBackGroupMember);
            }
        } else {
            businessUserMapper.updateByPrimaryKeySelective(businessUser);
        }
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName
     * @return
     */
    @Override
    public BusinessUser findByUserName(String userName) {
        return businessUserMapper.findByUserName(userName);
    }

    /**
     * 通过主键Bid查询
     *
     * @param bid
     * @return
     */
    @Override
    public BusinessUser findByBid(Long bid) {
        return businessUserMapper.selectByPrimaryKey(bid);
    }

    /**
     * 检查用户名是否存在:终端账户与调度员账户不可重复
     *
     * @param userName
     * @return
     */
    @Override
    public Boolean checkUser(String userName) {
        boolean flag = false;
        if (businessUserMapper.findByUserName(userName) == null && clientAccountMapper.findByAccountName(userName) == null) {
            flag = true;
        }
        return flag;
    }

    /**
     * 通过用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public BusinessUser findByUserNameAndPwd(String username, String password) {
        return businessUserMapper.findByUserNameAndPwd(username, password);
    }

    /**
     * 查询是否有重复账号
     *
     * @param businessId
     * @param userName
     * @return
     */
    @Override
    public BusinessUser findByBusinessIdAndUserName(int businessId, String userName) {
        return businessUserMapper.findByBusinessIdAndUserName(businessId, userName);
    }

    @Override
    public List<BusinessAreas> getAreas(String parentId) {
        return businessAreasMapper.getAreasByParentId(parentId);
    }

    /**
     * 根据状态查询调度员
     *
     * @param status
     * @return
     */
    @Override
    public PageInfo<DispatcherVo> findByStatus(Integer projectId, Integer status, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DispatcherVo> list = (status == null || status == 2) ? businessUserMapper.findByStatus(projectId, null) : businessUserMapper.findByStatus(projectId, status);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据所属项目查询调度员
     *
     * @param projectId
     * @return
     */
    @Override
    public PageInfo<BusinessUser> findByProjectId(Integer projectId, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<BusinessUser> list = businessUserMapper.selectByProjectId(projectId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据条件检索调度员信息
     *
     * @param type
     * @param param
     * @return
     */
    @Override
    public PageInfo<DispatcherVo> findByParam(Integer projectId, Integer type, String param, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DispatcherVo> list = null;
        switch (type) {
            case 0:
                list = businessUserMapper.findByParam(projectId, param, null, null);
                break;
            case 1:
                list = businessUserMapper.findByParam(projectId, null, param, null);
                break;
            case 2:
                list = businessUserMapper.findByParam(projectId, null, null, param);
                break;
        }
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 通过多个id导出多条数据
     * create by wsm on 2019-11-26
     *
     * @param bids     调度员id集合
     * @param response
     * @return
     */
    @Override
    public boolean exportDispatcherExcel(Integer projectId, List<Long> bids, HttpServletResponse response) {
        List<DispatcherVo> dispatcherVoList = businessUserMapper.selectByPrimaryKeyList(bids, projectId.longValue());
        iExcelService.exportExcel(dispatcherVoList, DispatcherVo.class, "调度员列表", response);
        return true;
    }

    /**
     * 根据终端账号查询终端所属项目下所有的调度员
     *
     * @param account
     * @return
     */
    @Override
    public List<BusinessUser> findDispatcherByAccount(String account) {
        return businessUserMapper.selectDispatcherByAccount(account);
    }


    /**
     * 总后台远程控制查询调度员信息
     * create by wsm on 2019-12-2
     *
     * @param myDispatcherVo
     * @return
     */
    @Override
    public PageInfo selectDispatcherByCondition(MyDispatcherVo myDispatcherVo) {
        myDispatcherVo.setAccountStatus("".equals(myDispatcherVo.getAccountStatus()) || myDispatcherVo.getAccountStatus() == null ? null : myDispatcherVo.getAccountStatus());
        myDispatcherVo.setOnlineStatus("".equals(myDispatcherVo.getOnlineStatus()) || myDispatcherVo.getOnlineStatus() == null ? null : myDispatcherVo.getOnlineStatus());
        PageHelper.startPage(myDispatcherVo.getPageNum(), myDispatcherVo.getPageSize());
        List<MyDispatcherVo> dispatcherByCondition = businessUserMapper.findDispatcherByCondition(myDispatcherVo);
        System.out.println("dispatcherByCondition===" + dispatcherByCondition);
        PageInfo pageInfo = new PageInfo(dispatcherByCondition);
        return pageInfo;
    }


    /**
     * 总后台更新企业信息
     *
     * @param businessUser
     * @return
     */
    @Override
    public boolean updateBusinessUser(BusinessUser businessUser) {
        businessUser.setUserType(0);//修改企业用户信息 {1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}
        int i = businessUserMapper.updateByTokenAndUserType(businessUser);
        userRealm.clearCache();
        return i >= 0;
    }

    @Override
    public void addSubManager(BusinessUser businessUser, Object[] members) throws Exception {
        this.addBusinessUser(businessUser);
        Long bid = businessUser.getBid();

        userRoleMapper.deleteByUserId(bid);
        UserRole userRole = null;
        //绑定角色
        for (int i = 0; i < members.length; i++) {
            userRole = new UserRole(bid, ((Integer) members[i]).longValue());
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public Map<String, String> getClientAccountByImei(String imei) {
        Long did = deviceInfoMapper.selectIdByImei(imei);
        if (did == null || did.equals(0)) {
            throw new SafetyHatException("未找到账号，登录失败");
        }
        List<ClientAccount> clientAccounts = clientAccountMapper.selectByDeviceId(did);
        if (clientAccounts == null || clientAccounts.size() == 0) {
            throw new SafetyHatException("未找到账号，登录失败");
        }
        Map<String, String> map = new HashMap<>(3);
        BusinessUser user = businessUserMapper.selectByPrimaryKey(clientAccounts.get(0).getBid());
        map.put("userName", user.getUserName());
        map.put("password", user.getPassword());
        return map;
    }
}
