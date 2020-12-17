package com.wyfx.total.controller;

import com.wyfx.total.entity.User;
import com.wyfx.total.exception.PasswordNotMatchException;
import com.wyfx.total.exception.UserNameNotMatchException;
import com.wyfx.total.license.LicenseVerify;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.service.UserService;
import com.wyfx.total.utile.MD5Util;
import com.wyfx.total.utile.RandomValidateCode;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description: 用户登录页面
 * @author： 张古良
 * @date 2019-11-2
 */
@RestController
@RequestMapping(value = "/login")
@Api(tags = "登录控制器")
public class UserController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ILogInfService iLogInfService;

    @GetMapping("/register")
    @ApiOperation(value = "注册接口", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "realname", value = "真实姓名", required = true, dataType = "String")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功")
    })
    public RespBean Reg(String username, String password, String realname) {
        logger.info("总后台注册账号");
        //查询用户名是否被占用
        User u = userService.findUserByUserName(username);
        Map map = new HashMap();
        if (u != null) {
            map.put("messages", "你所注册用户名已经存在");
        } else {
            User user = new User();
            String pwd = MD5Util.md5(password);
            user.setUname(username);
            user.setPassword(pwd);
            user.setName(realname);
            userService.addUser(user);
            map.put("messges", "注册成功");
        }
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 功能：用户登录
     *
     * @param userName
     * @param password
     * @return
     */
    @PostMapping(value = "/Login")
    @ApiOperation(value = "登录", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 201, message = "用户名,密码，验证码三者不能为空"),
            @ApiResponse(code = 202, message = "用户名错误"),
            @ApiResponse(code = 203, message = "验证码不匹配"),
            @ApiResponse(code = 204, message = "密码错误")
    })
    public RespBean handleLogin(String userName, String password, String code, @ApiIgnore HttpServletRequest request) {

        logger.info("登录前验证证书是否过期。");

//        //todo 验证证书是否安装或者证书是否过期只针对第三方部署企业
//        boolean boo = verifyLicense();
//        if (!boo) {
//            //todo 过期则暂停所有企业
//            //查询所有企业的并筛选出所有企业的bid集合
//            List<Map> bidList = enterpriseManagementService.selectAllBusinessASBid();
//            List bids = new ArrayList();
//            for (int i = 0; i < bidList.size(); i++) {
//                String bid = (String) bidList.get(i).get("bid");
//                bids.add(bid);
//            }
//            //暂停所有企业
//            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handleAutoPauseOperation", JSONArray.toJSONString(bids));
//            if ("false".equals(s)) {
//                logger.error("批量暂停企业后台合作状态失败 或更新数据失败");
//            }
//            return new RespBean(555, "证书验证失败，请联系管理员");
//        }


        logger.info("开始登录");
        if (userName.equals("") || password.equals("") || code.equals("")) {
            return new RespBean(201, "用户名,密码，验证码三者不能为空");
        } else {
            //判断验证码是否匹配
            String sCode = (String) request.getSession().getAttribute("codeValidate");
            System.out.println(sCode);
            if (!code.equalsIgnoreCase(sCode)) {
                return new RespBean(203, "验证码不匹配");
            } else {
                try {
                    User user = userService.getLogin(userName, password);
                    request.getSession().setAttribute("uid", user.getUid());
                    request.getSession().setAttribute("userName", user.getUname());
                    return new RespBean(ResponseCode.SUCCESS_CODE, user);
                } catch (PasswordNotMatchException e) {
                    return new RespBean(204, "密码错误");
                } catch (UserNameNotMatchException e) {
                    return new RespBean(202, "用户名错误");
                }
            }
        }
    }

    /**
     * 验证证书
     *
     * @return
     */
    public boolean verifyLicense() {
        LicenseVerify licenseVerify = new LicenseVerify();
        try {
            licenseVerify.verify();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "获取验证码", httpMethod = "GET", produces = "form-data")
    @GetMapping(value = "/getCode")
    public Object code(HttpServletRequest request, HttpServletResponse response) {
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

    /**
     * 修改用户名
     *
     * @param userName
     * @param session
     * @return
     */
    @PostMapping("/updateUserName")
    @ApiOperation(value = "修改用户名", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 201, message = "用户名不能为空"),
            @ApiResponse(code = 202, message = "用户名已经存在"),
    })
    public RespBean HandleUpdateUserName(String userName, @ApiIgnore HttpSession session) {
        logger.info("修改用户名");
        User u = new User();
        if (userName.equals("")) {
            return new RespBean(201, "用户名不能为空");
        }
        //查询用户名是否被占用
        User user = userService.findUserByUserName(userName);
        if (user != null) {
            return new RespBean(202, "用户名已经存在");
        }
        //设置需要修改用户名的用户uid
        u.setUid((Integer) session.getAttribute("uid"));
        u.setUname(userName);
        userService.updateUserName(u);
        iLogInfService.addLogInfRecord("修改用户名", 2, "修改人：" + session.getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, "姓名修改成功");

    }


    /**
     * 更新用户密码
     *
     * @param oldPwd
     * @param newPwd
     * @param verPwd
     * @param session
     * @return
     */
    @ApiOperation(value = "更新密码", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "旧密码", required = true),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true),
            @ApiImplicitParam(name = "verPwd", value = "确认密码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 201, message = "参数不为空"),
            @ApiResponse(code = 202, message = "初始密码不匹配"),
            @ApiResponse(code = 203, message = "两次输入密码不一致"),
            @ApiResponse(code = 204, message = "密码格式错误")
    })
    @PostMapping("/updatePassWord")
    public RespBean HandleUpdatePassWord(String oldPwd, String newPwd, String verPwd, @ApiIgnore(value = "session") HttpSession session) {
        logger.info("更新用户密码");
        if (oldPwd.equals("") || newPwd.equals("") || verPwd.equals("")) {
            return new RespBean(201, "不能为空");
        } else {
            //判断密码格式是否匹配6-20字符数字字符
            boolean b = newPwd.matches("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,20}$");
            if (b) {
                //判断两次密码是否匹配
                if (newPwd.equals(verPwd)) {
                    //判段原密码是否正确
                    Integer uid = (Integer) session.getAttribute("uid");
                    User user = userService.findUserById(uid);
                    String oldP = MD5Util.md5(oldPwd);
                    if (!user.getPassword().equals(oldP)) {
                        return new RespBean(202, "初始密码不匹配");
                    } else {
                        String newP = MD5Util.md5(newPwd);
                        user.setPassword(newP);
                        userService.updatePassword(user);
                        iLogInfService.addLogInfRecord("更新用户密码", 2, "更新人：" + session.getAttribute("userName"));
                        return new RespBean(ResponseCode.SUCCESS_CODE, "修改成功");
                    }
                } else {
                    return new RespBean(203, "两次输入密码不一致");
                }
            } else {
                return new RespBean(204, "密码格式错误");
            }
        }
    }


}
