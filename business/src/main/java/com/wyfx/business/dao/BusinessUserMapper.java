package com.wyfx.business.dao;

import com.wyfx.business.app.vo.ContactsForLocationVo;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.DispatcherVo;
import com.wyfx.business.entity.vo.MyDispatcherVo;
import com.wyfx.business.entity.vo.SubManagerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessUserMapper {
    int deleteByPrimaryKey(Long bid);

    int insert(BusinessUser record);

    int insertSelective(BusinessUser record);

    int updateNameOrPwd(@Param("bid") Long bid, @Param("name") String name, @Param("pwd") String pwd);

    int updateOnlineStatus(@Param("userName") String userName, @Param("status") int status);

    BusinessUser selectByPrimaryKey(Long bid);

    int updateByPrimaryKeySelective(BusinessUser record);

    int updateByPrimaryKey(BusinessUser record);

    BusinessUser findByUserName(String userName);

    List<BusinessUser> findByTokenAndBusinessId(@Param("token") String token);

    BusinessUser findByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);

    BusinessUser findByBusinessIdAndUserName(@Param("bid") int businessId, @Param("userName") String userName);

    List<BusinessUser> findByBusinessId(@Param("businessId") Long businessId);

    List<DispatcherVo> findByStatus(@Param("projectId") Integer projectId, @Param("status") Integer status);

    List<DispatcherVo> findByProjectId(@Param("projectId") Integer projectId, @Param("businessId") Integer businessId);

    List<BusinessUser> selectByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据终端账号查询调度员列表
     *
     * @param userName
     * @return
     */
    List<BusinessUser> selectDispatcherByAccount(@Param("userName") String userName);

    List<DispatcherVo> findByParam(@Param("projectId") Integer projectId, @Param("name") String name, @Param("userName") String userName, @Param("mobile") String mobile);

    /**
     * 查询app中通讯录中调度员终端用户
     *
     * @param projectId
     * @return
     */
    List<ContactsVo> findContactByProjectId(@Param("projectId") Integer projectId, @Param("bid") Long bid);

    List<ContactsForLocationVo> findContactAndLatLngByProjectId(@Param("projectId") Integer projectId, @Param("bid") Long bid);

    /**
     * 查询app中通讯录中调度员可终端用户
     *
     * @param workType
     * @return
     */
    List<ContactsVo> findContactByWorkType(@Param("projectId") Integer projectId, @Param("workType") Integer workType);

    /**
     * 查询app中通讯录中调度员可终端用户
     *
     * @param name
     * @return
     */
    List<ContactsVo> findContactByName(@Param("projectId") Integer projectId, @Param("name") String name);

    List<Long> findDispatcherByProjectId(@Param("projectId") Long projectId);


    /**
     * 通过多个id查询数据集合
     *
     * @param bids bid集合
     * @return
     */
    List<DispatcherVo> selectByPrimaryKeyList(@Param("bids") List<Long> bids, @Param("projectId") Long projectId);

    /**
     * 查询某一项目下的某一类型用户的具体成员ID信息
     *
     * @param projectId
     * @param userType
     * @return
     */
    List<Integer> selectByProjectIdAndUserType(@Param("projectId") Integer projectId, @Param("userType") Integer userType);


    /**
     * 通过状态查询子账号列表
     *
     * @param status
     * @return
     */
    List<SubManagerVo> findSubManagerByStatus(Integer status, Long businessId);


    /**
     * 通过用户id查询该用户的角色
     *
     * @param userId
     * @return
     */
    List<String> findSubManagerRoleUserId(Long userId);


    /**
     * 通过id查询子账号
     *
     * @param bid
     * @return
     */
    List<SubManagerVo> findSubManagerById(Integer bid);

    /**
     * 通过账号 姓名 电话检索
     *
     * @param username
     * @param name
     * @param mobile
     * @return
     */
    List<SubManagerVo> findByCondition(@Param("username") String username, @Param("name") String name, @Param("mobile") String mobile);

    /**
     * 总后台--远程控制 查询
     *
     * @param myDispatcherVo
     * @return
     */
    List<MyDispatcherVo> findDispatcherByCondition(MyDispatcherVo myDispatcherVo);

    /**
     * 通过 token和用户类型修改用户信息
     *
     * @param businessUser
     * @return
     */
    int updateByTokenAndUserType(BusinessUser businessUser);

    /**
     * 查询已启用终端账号数量
     *
     * @param token
     * @return
     */
    int selectUsedClientNumByBusinessId(@Param("token") String token);

}