package com.wyfx.business.dao;

import com.wyfx.business.entity.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FileInfoMapper {
    int deleteByPrimaryKey(Long fid);

    int deleteByFileName(@Param("fileName") String fileName);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(Long fid);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    int updateByName(@Param("fid") Long fid, @Param("fileName") String fileName);


    List<FileInfo> selectListByType(@Param("projectId") Long projectId, @Param("type") Integer type);

    /**
     * 在某一项目下根据账号类型和文件类型查询
     *
     * @param projectId
     * @param type
     * @param userType
     * @return
     */
    List<FileInfo> selectListByUserType(@Param("projectId") Long projectId, @Param("type") Integer type, @Param("userType") Integer userType);

    /**
     * 在某以项目下根据时间段查询
     *
     * @param
     * @param type
     * @param begin
     * @param end
     * @return
     */
    List<FileInfo> selectListByUploadTime(@Param("projectId") Long projectId, @Param("type") Integer type, @Param("begin") Date begin, @Param("end") Date end);

    /**
     * 在某以项目下根据文件名查询
     *
     * @param projectId
     * @param type
     * @param fileName
     * @return
     */
    List<FileInfo> selectListByFileName(@Param("projectId") Long projectId, @Param("type") Integer type, @Param("fileName") String fileName);

    List<FileInfo> selectListByAccount(@Param("projectId") Long projectId, @Param("type") Integer type, @Param("account") String account);

    List<FileInfo> selectListByName(@Param("projectId") Long projectId, @Param("type") Integer type, @Param("name") String name);

    /**
     * 导出记录使用---通过未接类型查询数据
     *
     * @param fileIds
     * @param type    文件类型{1:照片;2:视频}
     * @return
     */
    List<FileInfo> selectListByFileIds(@Param("fileIds") List<Long> fileIds, @Param("type") Integer type);
}