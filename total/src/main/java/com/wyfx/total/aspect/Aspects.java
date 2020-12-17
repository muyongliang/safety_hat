package com.wyfx.total.aspect;

import com.alibaba.fastjson.JSON;
import com.wyfx.total.entity.LogInfo;
import com.wyfx.total.service.ILogInfService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 创建日志切面
 * create by wsm on 2019-11-25
 */
@Aspect
@Component
@Deprecated
public class Aspects {//  此处为class 而不是aspect


    @Autowired
    private ILogInfService iLogInfService;


    @Pointcut("@annotation(com.wyfx.total.aspect.ControllerLog)")
    public void controllers() {
    }//此方法名随意


    @Pointcut("@annotation(com.wyfx.total.aspect.ServiceLog)")
    public void services() {
    }//此方法名随意


    /**
     * ProceedingJoinPoint 只支持Around  其他通知不支持
     *
     * @param joinPoint
     * @throws Exception
     */
    @AfterReturning("controllers()")//方法执行返回后执行切面
    public void controllerRecordLogs(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String executor = (String) request.getSession().getAttribute("userName");
        String type = getControllerMethodActionType(joinPoint);
        String description = getControllerMethodDescription(joinPoint);
        System.err.println("Around()=切面执行方法：" + description);
        Date ctime = new Date();
        LogInfo logInfo = new LogInfo(null, executor, Integer.parseInt(type), description, ctime, null);
        iLogInfService.addLogInfRecord(description, Integer.parseInt(type), null);
    }

    @AfterReturning("services()")
    public void serviceRecordLogs(JoinPoint joinPoint) throws Exception {
        String description = getServiceMethodMsg(joinPoint);
        System.err.println(description);
    }


    /***
     * 获取controller的操作信息
     * @param point
     * @return
     */
    public String getControllerMethodDescription(JoinPoint point) throws Exception {
        //获取连接点目标类名
        String targetName = point.getTarget().getClass().getName();
        //获取连接点签名的方法名
        String methodName = point.getSignature().getName();
        //获取连接点参数
        Object[] args = point.getArgs();
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        System.err.println("参数json=" + params);
        //根据连接点类的名字获取指定类
        Class targetClass = Class.forName(targetName);
        //获取类里面的方法
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    description = method.getAnnotation(ControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }


    /**
     * 获取controller 操作类型
     *
     * @param point
     * @return
     * @throws Exception
     */
    public String getControllerMethodActionType(JoinPoint point) throws Exception {
        //获取连接点目标类名
        String targetName = point.getTarget().getClass().getName();
        //获取连接点签名的方法名
        String methodName = point.getSignature().getName();
        //获取连接点参数
        Object[] args = point.getArgs();
        //根据连接点类的名字获取指定类
        Class targetClass = Class.forName(targetName);
        //获取类里面的方法
        Method[] methods = targetClass.getMethods();
        String actionType = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    actionType = method.getAnnotation(ControllerLog.class).actionType();
                    break;
                }
            }
        }
        return actionType;
    }


    /***
     * 获取service的操作信息
     * @param joinpoint
     * @return
     * @throws Exception
     */
    public String getServiceMethodMsg(JoinPoint joinpoint) throws Exception {
        //获取连接点目标类名
        String className = joinpoint.getTarget().getClass().getName();
        //获取连接点签名的方法名
        String methodName = joinpoint.getSignature().getName();
        //获取连接点参数
        Object[] args = joinpoint.getArgs();
        //根据连接点类的名字获取指定类
        Class targetClass = Class.forName(className);
        //拿到类里面的方法
        Method[] methods = targetClass.getMethods();
        String description = "";
        //遍历方法名，找到被调用的方法名
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    //获取注解的说明
                    description = method.getAnnotation(ServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

}
