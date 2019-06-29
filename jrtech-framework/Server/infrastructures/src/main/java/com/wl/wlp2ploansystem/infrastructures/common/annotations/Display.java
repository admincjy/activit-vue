package com.wl.wlp2ploansystem.infrastructures.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 中文显示注解
* 实体及实体字段元数据
* */
@Target(value={ElementType.PACKAGE,ElementType.TYPE,ElementType.METHOD,ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Display {

	/**
	* 显示内容
	* */
	String value() default "";

}
