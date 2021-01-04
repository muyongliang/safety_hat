package com.wyfx.business.service.common;

import com.wyfx.business.utils.poi.ExcelUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Transactional
public class IExcelServiceImpl implements IExcelService {


    /**
     * 导出excel文件
     *
     * @param list     导出的实体类集合
     * @param objClass 映射的实体类
     * @param fileName 导出的excel文件名
     * @param response HttpServletResponse
     */
    @Override
    @Transactional
    public void exportExcel(List<?> list, Class<?> objClass, String fileName, HttpServletResponse response) {
        try {
            ExcelUtils.exportExcel(list, null, "sheet1", objClass, fileName + ".xls", response);
        } catch (UnsupportedEncodingException e) {
            //todo 处理异常
            e.printStackTrace();
        }
    }


}
