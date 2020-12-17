package com.wyfx.business.service.common;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导入 导出excel通用接口
 * create by wsm on 2019-11-21
 */
public interface IExcelService {


    /**
     * 导出excel表
     *
     * @param list     导出的实体类集合
     * @param objClass 映射的实体类
     * @param fileName 导出的excel文件名
     * @param response HttpServletResponse
     */
    void exportExcel(List<?> list, Class<?> objClass, String fileName, HttpServletResponse response);

}
