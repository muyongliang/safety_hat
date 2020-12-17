package com.wyfx.total.mapper;

import com.wyfx.total.entity.Thirdbusinessmanager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ThirdbusinessmanagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Thirdbusinessmanager record);

    int insertSelective(Thirdbusinessmanager record);

    Thirdbusinessmanager selectByPrimaryKey(Integer id);

    /**
     * 通过第三方企业名查询企业信息
     *
     * @param bname
     * @return
     */
    Thirdbusinessmanager selectByTBusinessName(String bname);

    Thirdbusinessmanager selectTBusinessByBid(String bid);

    Thirdbusinessmanager selectByIpAndMac(@Param("ip") String ip, @Param("mac") String mac);

    Thirdbusinessmanager selectTBusinessByMainAccount(@Param("mainAccount") String mainAccount);


    /**
     * 按条件查询第三方企业信息
     *
     * @param state
     * @param bname
     * @param address
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map> selectTBusinessManagerByCondition(
            @Param("state") Integer state, @Param("bname") String bname, @Param("address") String address,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    /**
     * 查询所有第三方企业 时间降序排列
     *
     * @return
     */
    List<Map> selectAllTBusinessManager();

    Integer selectTBusinessManagerCount();

    /**
     * 通过bid 更新第三方企业
     *
     * @param record
     * @return
     */
    int updateTBusinessByBid(Thirdbusinessmanager record);

    int updateByPrimaryKeySelective(Thirdbusinessmanager record);

    int updateByPrimaryKeyWithBLOBs(Thirdbusinessmanager record);

    int updateByPrimaryKey(Thirdbusinessmanager record);

    int updateTBusinessStatusByBids(@Param("bids") List<String> bids, @Param("state") Integer state);


}