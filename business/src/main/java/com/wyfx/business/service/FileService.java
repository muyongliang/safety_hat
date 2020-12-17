package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.FileInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FileService {
    /**
     * 新增文件信息
     *
     * @param fileInfo
     */
    void addFileInfo(FileInfo fileInfo);

    /**
     * 修改文件名
     *
     * @param fid
     * @param fileName
     */
    void updateFileName(Long fid, String fileName);

    String getUrlById(Long id, HttpServletRequest request);

    FileInfo getByFid(Long id);

    PageInfo<FileInfo> getList(Long projectId, Integer type, Integer pageSize, Integer pageNum);

    PageInfo<FileInfo> getListByType(Long projectId, Integer type, Integer userType, Integer pageSize, Integer pageNum);

    PageInfo<FileInfo> getListByUploadTime(Long projectId, Integer type, String begin, String end, Integer pageSize, Integer pageNum) throws Exception;

    PageInfo<FileInfo> getListByFileName(Long projectId, Integer type, String fileName, Integer pageSize, Integer pageNum);

    PageInfo<FileInfo> getListByAccount(Long projectId, Integer type, String account, Integer pageSize, Integer pageNum);

    PageInfo<FileInfo> getListByName(Long projectId, Integer type, String name, Integer pageSize, Integer pageNum);

    void deleteFile(Long fid);

    void deleteFile(String fids);

    /**
     * 查询文件信息--导出数据使用
     *
     * @param fileIds
     * @return
     */
    List<FileInfo> selectFileList(List<Long> fileIds, Integer type);

}
