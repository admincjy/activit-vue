package com.wl.wlp2ploansystem.infrastructures.common.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 类型工具类
 */

public  class TypeHelper {

  static List<String> types =   Arrays.asList("java.lang.Integer",  
	        "java.lang.Double",  
	        "java.lang.Float",  
	        "java.lang.Long",  
	        "java.lang.Short",  
	        "java.lang.Byte",  
	        "java.lang.Boolean",  
	        "java.lang.Character",  
	        "java.lang.String",  
	        "int","double","long","short","byte","boolean","char","float");

	/**
	 * 是否简单类型
	 */
	public static boolean isPrimitive(Class<?> type){
		 String fieldTypeString = type.getTypeName();
		 return types.contains(fieldTypeString);
	}
	/**
	 * 获取异常堆栈字符串
	 */
	public static String getExcetionStackTrace(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}
