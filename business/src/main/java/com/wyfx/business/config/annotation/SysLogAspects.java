package com.wyfx.business.config.annotation;

import com.wyfx.business.controller.BaseController;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Log;
import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.vo.ClientAccountVo;
import com.wyfx.business.entity.vo.SubManagerVo;
import com.wyfx.business.service.common.ILogService;
import com.wyfx.business.service.shiro.BusinessUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author johnson liu
 */
@Aspect
@Component
public class SysLogAspects {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspects.class);
    @Autowired
    private ILogService iLogService;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private BaseController baseController;

    public SysLogAspects() {
    }

    @Pointcut("@annotation(com.wyfx.business.config.annotation.AopLog)")
    public void logPointCut() {
    }

    @After("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String describe = null;
        OperationType operationType = null;
        String targetParamName = null;
        AopLog aopLog = method.getAnnotation(AopLog.class);
        if (aopLog != null) {
            describe = aopLog.describe();
            operationType = aopLog.operationType();
            targetParamName = aopLog.targetParamName();
        }

        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> paramsMap = new HashMap<>(paramNames.length);

        for (int i = 0; i < args.length; ++i) {
            //排除文件上传参数/HttpServletRequest/HttpServletResponse
            if (!(args[i] instanceof ServletRequest) && !(args[i] instanceof ServletResponse) && !(args[i] instanceof MultipartFile)) {
                paramsMap.put(paramNames[i], args[i]);
            }
        }

        BusinessUser user = null;
        StringBuffer stringBuffer = new StringBuffer(describe);
        if (false) {/*operationType == OperationType.LOGIN*/
            user = businessUserService.findByUserName(paramsMap.get(targetParamName).toString());
            if (user != null) {
                stringBuffer.append(user.getUserName());
            }
        } else {
            user = baseController.getCurrentUser();
            if (targetParamName != null && !"".equals(targetParamName)) {
                Object obj = paramsMap.get(targetParamName);
                if (null != obj && obj instanceof String) {
                    stringBuffer.append(obj.toString());
                }
                if (null != obj && obj instanceof ProjectInfo) {
                    stringBuffer.append(((ProjectInfo) obj).getProjectName());
                }
                if (null != obj && obj instanceof ClientAccountVo) {
                    stringBuffer.append(((ClientAccountVo) obj).getAccount());
                }
                if (null != obj && obj instanceof SubManagerVo) {
                    stringBuffer.append(((SubManagerVo) obj).getUsername());
                }

            }
        }
        if (user != null) {
            String detail = stringBuffer.toString();
            Log log = new Log(null, user.getUserType(), user.getName(), operationType.getType(), operationType.getName(), detail, new Date(), user.getBusinessId(), user.getBid());
            iLogService.addLog(log);
        }
    }
}
