package com.wl.wlp2ploansystem.infrastructures.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.IPHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.JsonHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.infrastructures.common.support.TypeHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
* 系统日志切面
* */
@Component
@Aspect
public class LoggingAspect {

    private static LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    private  LoggingService loggingService;

    /**
     * 具有RequestMapping注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void springBeanPointcut_RequestMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    /**
     * 具有PostMapping注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void springBeanPointcut_PostMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    /**
     * 具有GetMapping注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void springBeanPointcut_GetMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    /**
     * 具有PutMapping注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void springBeanPointcut_PutMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    /**
     * 具有DeleteMapping注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void springBeanPointcut_DeleteMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    /**
     * 具有PatchMappingg注解标注的方法会被拦截记录日志
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void springBeanPointcut_PatchMapping() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("springBeanPointcut_RequestMapping()||springBeanPointcut_PostMapping()||springBeanPointcut_GetMapping()||springBeanPointcut_PutMapping()||springBeanPointcut_DeleteMapping()||springBeanPointcut_PatchMapping()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long beginTime = System.currentTimeMillis();

        Object result = null;
        Throwable ex =null;
        try {
            result = joinPoint.proceed();

        } catch (Throwable e) {
            ex =e;
        }

        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        log(joinPoint,ex, time);

        if(ex !=null){
            throw ex;
        }

        return result;
    }


    private void log(JoinPoint joinPoint,Throwable e, long duration) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log syslog = method.getAnnotation(Log.class);
        if(syslog != null && syslog.disabled()){
            return;
        }

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        //请求的方法名
        String methodFullName = className + "." + methodName + "()";
        LoggingDetails loggingDetails = new LoggingDetails();

        if(syslog != null){
            //注解上的描述
            loggingDetails.setOperation(syslog.value());
        }else{
            loggingDetails.setOperation(methodFullName);
        }

        loggingDetails.setMethod(methodFullName);

        Object[] joinPointArgs =  joinPoint.getArgs();
        if(joinPointArgs!=null) {
            //请求的参数
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

            Map<String,Object> parameterMap = new HashMap<>();
            for(int i=0;i<parameterNames.length;i++){
                Object paramenterValue = joinPointArgs[i];

                if(paramenterValue.getClass().isAssignableFrom(HttpServletRequest.class)
                        || paramenterValue.getClass().isAssignableFrom(HttpServletResponse.class)
                        ){
                    continue;

                }
                String parameterName = parameterNames[i];
                parameterMap.put(parameterName,paramenterValue);
            }

            String params = "";
            try {
                params = JsonHelper.map2Json(parameterMap, true);
            }
            catch (Exception ex){
                params = parameterMap.toString();
            }
            int paramsLength = Math.min(params.length(), 1000);
            String dbparams = StringUtils.isEmpty(params) ? null : params.substring(0, paramsLength);
            loggingDetails.setParams(dbparams);
        }

        //获取request
        HttpServletRequest request = SpringContextUtil.getHttpServletRequest();
        if(request !=null) {
            //设置IP地址
            loggingDetails.setIp(IPHelper.getIpAddr(request));
        }

        //用户名
        DomainUserDetails currentUser = SecurityUtils.getCurrentUser();
        if(currentUser !=null){
            String userId  = currentUser.getId();
            loggingDetails.setUserId(userId);
        }

        loggingDetails.setExecuteTime(LocalDateTime.now());
        if(e !=null) {
            loggingDetails.setResult("error");
            if(!StringUtils.isEmpty(e.getMessage())) {
                int messageLength = Math.min(e.getMessage().length(), 1000);
                String errorMessage = StringUtils.isEmpty(e.getMessage()) ? null : e.getMessage().substring(0, messageLength);
                loggingDetails.setErrorMessage(errorMessage);
            }

            String stackTraceFullMessage = TypeHelper.getExcetionStackTrace(e);
            if(!StringUtils.isEmpty(stackTraceFullMessage)) {
                int stackTraceLength = Math.min(stackTraceFullMessage.length(), 4000);
                String stackTrace = StringUtils.isEmpty(stackTraceFullMessage) ? null : stackTraceFullMessage.substring(0, stackTraceLength);
                loggingDetails.setErrorStackTrace(stackTrace);
            }
        }
        else{
            loggingDetails.setResult("success");
        }
        loggingDetails.setDuration(duration);
        //保存系统日志
        loggingService.save(loggingDetails);

    }


}