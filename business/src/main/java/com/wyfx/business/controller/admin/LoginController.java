package com.wyfx.business.controller.admin;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.alarmRange.util.HttpClientUtil;
import com.wyfx.business.config.MyUsernamePasswordToken;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.dao.ZidianSettingMapper;
import com.wyfx.business.entity.*;
import com.wyfx.business.entity.vo.DiySetVo;
import com.wyfx.business.service.*;
import com.wyfx.business.service.common.IBusinessInfoService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.utils.AccountUtil;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.RandomValidateCode;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author johnson liu
 */
@Controller
@RequestMapping("/admin")
@Api(value = "LoginController", tags = {"用户登录退出等API"})
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IBusinessInfoService iBusinessInfoService;
    @Autowired
    private ClientAccountService clientAccountService;
    @Autowired
    private TalkBackService talkBackService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AlarmSettingService alarmSettingService;
    @Autowired
    private ZidianSettingMapper zidianSettingMapper;

    /**
     * shiro重定向
     *
     * @return
     */
    @ApiOperation(value = "客户端重定向到登录页面接口", httpMethod = "GET", produces = "form-data")
    @RequestMapping(value = "/toIndex", method = RequestMethod.GET)
    @ResponseBody
    public Object toIndex() {
        return new MyResponseEntity(ResponseCode.ERROR_SESSION.getValue(), "会话超时，请重新登陆");
    }

    @ApiOperation(value = "服务端重定向到index.html页面", httpMethod = "GET", produces = "form-data")
    @RequestMapping(value = "/toIndexPage", method = RequestMethod.GET)
    public Object toIndexPage() {
        return "redirect:/index.html";
    }


    /**
     * 登录验证,同一账号不支持同一端多出登录，但支持pc和移动端同时在线,且只有调度员可以进行移动端登录
     *
     * @param source {0:web;1:"调度员";2:"终端",3:总后台}
     * @return
     */
    @ApiOperation(value = "用户登录", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "账户密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "source", value = "来源,{0:web;1:\"调度员\";2:\"终端\",3:总后台}", required = true, dataType = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 402, message = "未知账号"),
            @ApiResponse(code = 403, message = "用户名或密码错误"),
            @ApiResponse(code = 404, message = "账号已被禁用"),
            @ApiResponse(code = 50004, message = "输入参数错误"),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, String verifyCode, Integer source, HttpServletRequest request) {
        if (username == null || password == null) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数不能为空");
        }
        if (source == 0) {
            /*if (!RandomValidateCode.verify(verifyCode,getSubject())) {
                System.out.println("shiro验证为通过");
            }*/
            if (!RandomValidateCode.verify(verifyCode, request)) {
                return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "验证码错误");
            }
        }
        password = MD5Util.encrypt(username.toLowerCase(), password);
        System.out.println(password);
        BusinessUser businessUser = (BusinessUser) getSubject().getPrincipal();
        Boolean isLogin = businessUser != null && businessUser.getUserName().equals(username);
        //判断是否有用户登录


        if (getSubject().isAuthenticated() && isLogin) {
            getSubject().logout();//退出之前登录账号
        }

        if (!getSubject().isAuthenticated()) {
            /*UsernamePasswordToken token = new UsernamePasswordToken(username, password);*/
            MyUsernamePasswordToken token = new MyUsernamePasswordToken(username, password, source);
            try {
                super.login(token);
                getSession().setAttribute("source", source);
            } catch (UnknownAccountException uae) {
                return new MyResponseEntity(402, uae.getMessage());
            } catch (IncorrectCredentialsException ice) {
                return new MyResponseEntity(403, "用户名或密码错误,请重试");
            } catch (LockedAccountException lae) {
                return new MyResponseEntity(404, "该账号已被禁用,无法登陆");
            } catch (AuthenticationException a) {
                return new MyResponseEntity(403, "认证失败");
            }
        }

        BusinessUser user = businessUserService.findByUserName(username);
        boolean equals = MD5Util.encrypt(username.toLowerCase(), user.getPassword()).equals(password);
        if (!equals) {
            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "密码错误，请重新输入密码");
        }
        if (user.getUserType() == 1) {
            SecurityUtils.getSubject().getSession().setTimeout(-1000L);
        } else {
            SecurityUtils.getSubject().getSession().setTimeout(30 * 60 * 1000);
        }
        log.info(username + "接口获取sessionId:" + request.getSession().getId() + "\nshiro获取sessionId:" + getSubject().getSession().getId());
        TalkBackGroup talkBackGroup = (user != null) ? talkBackService.findClientTalkBack(user) : null;

        Map<String, Object> map = new HashMap<>();
        Long groupId = null;
        String groupName = null;
        if (talkBackGroup != null) {
            groupId = talkBackGroup.getId();
            groupName = talkBackGroup.getName();
        }
        map.put("groupId", groupId);
        map.put("groupName", groupName);
        map.put("projectId", user.getProjectId());
        map.put("config", getBusinessOfSetting(user));
        map.put("userName", user.getUserName());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), user.getName(), map);
    }


    /**
     * todo 测试免密登录
     * source固定为666时免密登录
     *
     * @param username
     * @param
     * @return
     */
    @ApiOperation(value = "总后台免密登录", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "source", value = "来源,{0:web;1:\"调度员\";2:\"终端\",3:总后台}", required = true, dataType = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 402, message = "未知账号"),
            @ApiResponse(code = 403, message = "用户名或密码错误"),
            @ApiResponse(code = 404, message = "账号已被禁用"),
            @ApiResponse(code = 50004, message = "输入参数错误"),
    })
    @PostMapping("/toLogin")
    @ResponseBody
    public MyResponseEntity toLogin(String username, Integer source) {
        MyUsernamePasswordToken token = new MyUsernamePasswordToken(username, source);
        BusinessUser user = null;
        try {
            super.login(token);
            getSession().setAttribute("source", source);
            user = businessUserService.findByUserName(username);
        } catch (UnknownAccountException uae) {
            return new MyResponseEntity(402, uae.getMessage());
        } catch (IncorrectCredentialsException ice) {
            return new MyResponseEntity(403, "用户名或密码错误,请重试");
        } catch (LockedAccountException lae) {
            return new MyResponseEntity(404, "该账号已被禁用,无法登陆");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), user.getName());
    }


    private Map getBusinessOfSetting(BusinessUser user) {
        String token = JSON.toJSONString(iBusinessInfoService.findByBusinessId(user.getBusinessId()).getToken());
        String s = HttpClientUtil.doPostJson("http://" + remoteUrl + "/enterprise/handleSelectBusinessManagerDiySetting", token);
        DiySetVo diySetVo = JSON.parseObject(s, DiySetVo.class);
        Map map = null;
        try {
            map = AccountUtil.convertObjectToMap(diySetVo);
            String width = "";
            String height = "";
            Integer kps = null;
            if (user.getUserType() == 2) {
                ClientAccount clientAccount = clientAccountService.findByBid(user.getBid().intValue());
                String resolution = clientAccount.getResolution();
                int index = (resolution == null) ? 0 : resolution.indexOf("*");
                if (index == -1) {
                    /*resolution=*/
                }
                if (index > 0) {
                    width = resolution.substring(0, index);
                    height = resolution.substring(index + 1);
                    kps = clientAccount.getMaxKps();
                }
            }
            map.put("width", width);
            map.put("height", height);
            map.put("kps", kps);
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }

    /**
     * 终端用户登录
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "终端用户登录", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "imei", value = "设备IMEI号", required = true, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 402, message = "未知账号"),
            @ApiResponse(code = 403, message = "用户名或密码错误"),
            @ApiResponse(code = 404, message = "账号已被禁用"),
            @ApiResponse(code = 50004, message = "输入参数错误"),
    })
    @RequestMapping(value = "/client/login", method = RequestMethod.POST)
    @ResponseBody
    public Object clientLogin(String username, String password, String imei, HttpServletRequest request) throws Exception {
        if (username == null || password == null) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数不能为空");
        }
        password = MD5Util.encrypt(username.toLowerCase(), password);

        //判断用户是否登录
        if (!getSubject().isAuthenticated()) {
            /*UsernamePasswordToken token = new UsernamePasswordToken(username, password);*/
            MyUsernamePasswordToken token = new MyUsernamePasswordToken(username, password, 2, imei);
            try {
                super.login(token);
                getSession().setAttribute("source", 2);
            } catch (UnknownAccountException uae) {
                return new MyResponseEntity(402, uae.getMessage());
            } catch (IncorrectCredentialsException ice) {
                return new MyResponseEntity(403, ice.getMessage());
            } catch (LockedAccountException lae) {
                return new MyResponseEntity(404, "该账号已被禁用,无法登陆");
            } catch (AuthenticationException a) {
                return new MyResponseEntity(403, "认证失败");
            }
        }
        //-- 保存登录日志
        BusinessUser businessUser = (BusinessUser) getSubject().getPrincipal();
        log.info(username + "接口获取sessionId:" + request.getSession().getId() + "\nshiro获取sessionId:" + getSubject().getSession().getId());
        //设置session用不过期
        SecurityUtils.getSubject().getSession().setTimeout(-1000L);
        Map clientInfo = clientAccountService.getCurrentClientInfo(businessUser.getBid());
        DeviceInfo device = deviceService.findDetailByBid(businessUser.getBid());
        Map<String, Object> map = new HashMap<>();
        map.put("name", businessUser.getName());
        map.put("bid", businessUser.getBid());
        map.put("config", getBusinessOfSetting(businessUser));
        map.put("alarmSetting", alarmSettingService.findByProjectId(businessUser.getProjectId().intValue()));

        String groupName = (clientInfo != null) ? clientInfo.get("talk_back_name").toString() : null;
        Long groupId = (clientInfo != null) ? Long.valueOf(clientInfo.get("id").toString()) : null;
        boolean isInGrouping = clientInfo != null;
        String number = (device != null) ? device.getNumber() : null;

        map.put("groupName", groupName);
        map.put("groupId", groupId);
        map.put("number", number);
        map.put("isInGrouping", isInGrouping);
        map.put("workType", zidianSettingMapper.selectByPrimaryKey(businessUser.getZidianId()).getName());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


    /**
     * 获取验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码", httpMethod = "GET", produces = "form-data")
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public Object captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*CaptchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);*/
        response.setContentType("image/jpeg");//设置响应类型，告知浏览器输出的是图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Set-Cookie", "name=value; HttpOnly");//设置HttpOnly属性,防止Xss攻击
        response.setDateHeader("Expire", 0);
        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
            randomValidateCode.getRandomCode(request, response);//生成图片并通过response输出
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @ApiOperation(value = "退出登录", httpMethod = "GET", produces = "form-data")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Object logout() {
        super.getSubject().logout();
        //重定向到登录页面
        /*return "redirect:/login.html";*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 下拉联动，获取区域信息
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getAreas", method = RequestMethod.GET)
    @ResponseBody
    @ApiIgnore
    public Object getAreas(String parentId) {
        List<BusinessAreas> list = businessUserService.getAreas(parentId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    @RequestMapping("/getAccount")
    @ResponseBody
    public Object getAccount(String imei) {
        Map<String, String> account = businessUserService.getClientAccountByImei(imei);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), account);
    }
}
