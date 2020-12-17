package com.wyfx.total.controller;

import com.wyfx.total.utile.FilePathUtil;
import com.wyfx.total.utile.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/test")
@Api(tags = "测试控制器")
public class TestController {


    public static Date returnBefore24HAgo(Integer number, Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //正数代表未来某个时间段 负数代表当前时间前某个时间段
        calendar.add(Calendar.DAY_OF_MONTH, -number);
        Date startT = calendar.getTime();
        System.err.println("returnBefore24HAgo=" + sdf.format(startT));
        return startT;
    }

    @GetMapping("/t1")
    @ApiOperation(value = "测试1")
    @ApiResponse(message = "字符串", code = 200)
    public RespBean t1(@RequestParam(value = "data") @ApiParam("数据") String data) {
        System.err.println(data);
        return new RespBean(200);
    }

    @GetMapping("/t2")
    public RespBean t2(@RequestBody String data) {
        System.err.println(data);
        return new RespBean(200);
    }

    @GetMapping("/test")
    public RespBean test(HttpServletRequest request) {
        long l = System.currentTimeMillis();
        String baseUrl = FilePathUtil.getBaseUrl(request);
        long l1 = System.currentTimeMillis();
        System.err.println(l1 - l);
        return new RespBean(200, baseUrl);
    }


}
