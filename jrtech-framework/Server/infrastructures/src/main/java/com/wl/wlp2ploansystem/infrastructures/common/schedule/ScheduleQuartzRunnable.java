package com.wl.wlp2ploansystem.infrastructures.common.schedule;

import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行定时任务
 *
 */
public class ScheduleQuartzRunnable implements Runnable {
    private Object target;
    private Method method;
    private String params;

    public ScheduleQuartzRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = SpringContextUtil.getBean(beanName);
        this.params = params;

        if(StringUtils.isNotBlank(params)){
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        }else{
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run()  {
        try {
            ReflectionUtils.makeAccessible(method);
            if(StringUtils.isNotBlank(params)){
                method.invoke(target, params);
            }else{
                method.invoke(target);
            }
        } catch (IllegalAccessException e) {
           throw new RuntimeException("IllegalAccessException",e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("InvocationTargetException",e);
        }
    }

}