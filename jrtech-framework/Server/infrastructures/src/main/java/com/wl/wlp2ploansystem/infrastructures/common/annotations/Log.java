package com.wl.wlp2ploansystem.infrastructures.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 系统日志注解
* 此注解标注的Controller会记录在系统操作日志中
* */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
    * 是否禁用
    * */
    boolean disabled() default  false;
    /**
     * 显示名称
     * */
    String value() default "";

}
