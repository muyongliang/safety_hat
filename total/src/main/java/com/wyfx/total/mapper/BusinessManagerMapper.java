package com.wyfx.total.mapper;

import com.wyfx.total.entity.BusinessManager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface BusinessManagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessManager record);

    int insertSelective(BusinessManager record);

    BusinessManager selectByPrimaryKey(Integer id);

    BusinessManager selectByBusinessKey(String bid);

    /**
     * 通过企业名查询企业信息
     *
     * @param bName
     * @return
     */
    BusinessManager selectByBusinessName(String bName);

    /**
     * 通过企业名模糊查询
     *
     * @param bname
     * @return
     */
    List<BusinessManager> selectLikeBname(String bname);


    /**
     * 通过主账号查询企业信息
     *
     * @param mainAccount
     * @return
     */
    BusinessManager selectBusinessByMainAccount(String mainAccount);

    /**
     * 按条件筛选企业信息
     *
     * @param status
     * @param address
     * @param bname
     * @param mainAccount
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map> selectBusinessManagerByCondition(
            @Param("status") Integer status, @Param("address") String address, @Param("bname") String bname,
            @Param("mainAccount") String mainAccount, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 查询已经到期24小时以上的企业
     *
     * @param status
     * @param validityTime
     * @return
     */
    List<Map> selectNoValidityBusinessManager(@Param("status") Integer status, @Param("validityTime") Date validityTime);


    List<BusinessManager> selectBusinessManager(BusinessManager record);

    /**
     * 查询所有企业信息
     * 2019-11-11
     */
    List<Map> selectAllBusinessManager();

    /**
     * 通过合作状态和企业名 查询
     *
     * @param status
     * @return
     */
    List<Map> selectAllByStatusAndBname(Integer status, String bname);


    Integer selectEnterpriseCount();

    int selectEnterpriseStatisticalCount(BusinessManager record);

    int updateByPrimaryKeySelective(BusinessManager record);

    //通过bid更新企业信息
    int updateByBusinessIdSelective(BusinessManager record);

    int updateByPrimaryKeyWithBLOBs(BusinessManager record);

    int updateByPrimaryKey(BusinessManager record);

    int updateBusinessStatusByBid(@Param("bids") List<String> bids, @Param("status") Integer status);

}