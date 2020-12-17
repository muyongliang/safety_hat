package com.wyfx.total.controller;

import com.wyfx.total.entity.Files;
import com.wyfx.total.utile.ExcelUtils;
import com.wyfx.total.utile.RespBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
@Deprecated  //测试导入导出excel
@ApiIgnore
public class ExcelController {

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public RespBean exportExcel(HttpServletResponse response) throws UnsupportedEncodingException {
        System.err.println("导出");
        List<Files> personList = new ArrayList<Files>();
        Files person1 = new Files();
        Files person2 = new Files();
        person1.setFileId("1");
        person2.setFileId("2");
        person1.setFileName("女");
        person2.setFileName("男");
        person1.setTags("谷向南是大老娘们");
        person2.setTags("你好 世界");
        personList.add(person1);
        personList.add(person2);
        //导出操作 标题可以为空
        ExcelUtils.exportExcel(personList, null, "sheet", Files.class, "文件名.xls", response);
        return new RespBean(200, "ok");
    }


    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public void importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        System.err.println("前端上传的文件为：" + file);//用来检查前端是否把文件传过来
        //解析excel，如果有title和header行则添加 占几行则填写几行
        List<Files> personList = ExcelUtils.importExcel(file, 1, 1, Files.class);
        System.out.println("导入数据一共【" + personList.size() + "】行");
        //保存数据库
        for (Files files : personList) {
            System.out.println(files);
        }

    }
}
