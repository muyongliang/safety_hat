package com.wyfx.business.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.FileInfoMapper;
import com.wyfx.business.entity.FileInfo;
import com.wyfx.business.utils.DateUtil;
import com.wyfx.business.utils.FilePathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author johnson liu
 * @date 2019/12/3
 * @description 文件业务类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 新增文件信息
     *
     * @param fileInfo
     */
    @Override
    @Transactional
    public void addFileInfo(FileInfo fileInfo) {
        fileInfoMapper.insertSelective(fileInfo);
    }

    /**
     * 修改文件名
     *
     * @param fid
     * @param fileName
     */
    @Override
    @Transactional
    public void updateFileName(Long fid, String fileName) {
        fileInfoMapper.updateByName(fid, fileName);
    }

    @Override
    public String getUrlById(Long id, HttpServletRequest request) {
        FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(id);
        if (fileInfo == null) {
            return null;
        }
        String url = FilePathUtil.getBaseUrl(request) + fileInfo.getUrl();
        return url;
    }

    @Override
    public FileInfo getByFid(Long id) {
        return fileInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<FileInfo> getList(Long projectId, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = fileInfoMapper.selectListByType(projectId, type);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo getListByType(Long projectId, Integer type, Integer userType, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = fileInfoMapper.selectListByUserType(projectId, type, userType);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<FileInfo> getListByUploadTime(Long projectId, Integer type, String begin, String end, Integer pageSize, Integer pageNum) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = null;
        try {
            Date beginTime = (begin == null || "".equals(begin)) ? DateUtil.formatDate(DateUtil.getCurrentDate()) : DateUtil.formatDate(DateUtil.completionDate(begin));
            Date endTime = (end == null || "".equals(end)) ? new Date() : DateUtil.formatDate(DateUtil.completionDate(end));
            list = fileInfoMapper.selectListByUploadTime(projectId, type, beginTime, endTime);
        } catch (Exception e) {
            throw e;
        }
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<FileInfo> getListByFileName(Long projectId, Integer type, String fileName, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = fileInfoMapper.selectListByFileName(projectId, type, fileName);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<FileInfo> getListByAccount(Long projectId, Integer type, String account, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = fileInfoMapper.selectListByAccount(projectId, type, account);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<FileInfo> getListByName(Long projectId, Integer type, String name, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> list = fileInfoMapper.selectListByName(projectId, type, name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    @Transactional
    public void deleteFile(Long fid) {
        fileInfoMapper.deleteByPrimaryKey(fid);
    }

    @Override
    public void deleteFile(String fids) {
        System.out.println(fids);
        List<Long> list = Arrays.stream(fids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        for (Long id : list) {
            fileInfoMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 查询照片或视频的数据 ---导出记录
     *
     * @param fileIds
     * @param type
     * @return
     */
    @Override
    public List<FileInfo> selectFileList(List<Long> fileIds, Integer type) {
        List<FileInfo> fileInfos = fileInfoMapper.selectListByFileIds(fileIds, type);
        return fileInfos;
    }
}
