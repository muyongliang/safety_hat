package com.wyfx.business.service.shiro;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.BusinessAreas;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.DispatcherVo;
import com.wyfx.business.entity.vo.MyDispatcherVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BusinessUserService {
    void updateOnlineStatus(String userName, int status);

    void updateNameOrPwd(Long bid, String name, String pwd);

    /**
     * 移动账号到指定项目
     *
     * @param projectId
     * @param clientId
     */
    void moveToTargetProject(Integer projectId, Long[] clientId, BusinessUser currentUser) throws Exception;

    boolean checkAccount(String userName, Long businessId);

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     */
    void addUser(String username, String password);

    /**
     * 添加用户
     *
     * @param businessUser
     */
    void addBusinessUser(BusinessUser businessUser) throws Exception;

    /**
     * 通过用户名查询用户
     *
     * @param userName
     * @return
     */
    BusinessUser findByUserName(String userName);

    /**
     * 通过主键Bid查询
     *
     * @param bid
     * @return
     */
    BusinessUser findByBid(Long bid);

    /**
     * 检查用户名是否存在:终端账户与调度员账户不可重复
     *
     * @param name
     * @return
     */
    Boolean checkUser(String name);

    /**
     * 通过用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    BusinessUser findByUserNameAndPwd(String username, String password);

    /**
     * 查询是否有重复账号
     *
     * @param businessId
     * @param userName
     * @return
     */
    BusinessUser findByBusinessIdAndUserName(int businessId, String userName);

    List<BusinessAreas> getAreas(String parentId);

    /**
     * 根据状态查询调度员
     *
     * @param status
     * @return
     */
    PageInfo<DispatcherVo> findByStatus(Integer projectId, Integer status, Integer pageSize, Integer pageNum);

    /**
     * 根据所属项目查询调度员
     *
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfo<BusinessUser> findByProjectId(Integer projectId, Integer pageSize, Integer pageNum);

    /**
     * 根据条件检索调度员信息
     *
     * @param type
     * @param param
     * @return
     */
    PageInfo<DispatcherVo> findByParam(Integer projectId, Integer type, String param, Integer pageSize, Integer pageNum);

    /**
     * 通过id列表导出多条数据
     *
     * @param bids
     * @return
     */
    boolean exportDispatcherExcel(Integer projectId, List<Long> bids, HttpServletResponse response);


    /**
     * 根据终端账号查询终端所属项目下所有的调度员
     *
     * @param account
     * @return
     */
    List<BusinessUser> findDispatcherByAccount(String account);


    /**
     * 总后台 远程控制查询
     *
     * @param myDispatcherVo
     * @return
     */
    PageInfo selectDispatcherByCondition(MyDispatcherVo myDispatcherVo);

    /**
     * 总后台 更新企业用户信息
     *
     * @return
     */
    boolean updateBusinessUser(BusinessUser businessUser);

    void addSubManager(BusinessUser businessUser, Object[] members) throws Exception;

    Map<String, String> getClientAccountByImei(String imei);
}
