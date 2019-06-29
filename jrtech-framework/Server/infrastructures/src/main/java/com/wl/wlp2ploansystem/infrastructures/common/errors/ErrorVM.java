package com.wl.wlp2ploansystem.infrastructures.common.errors;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统错误前端返回传输对象
 */
@Data
public class ErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 错误编码
	 */
    private String code;
	/**
	 * 错误内容
	 */
	private String message;
	/**
	 * 错误详细
	 */
	private String details;

	/***
	 * 额外参数
	 */
	private Object payload;
	
	public ErrorVM(String code,String message, String details,Object payload) {
		this.code = code;
		this.message = message;
		this.details = details;
		this.payload = payload;
	}
	public ErrorVM(String message,String details) {
		this(null ,message, details,null);
	}
	public ErrorVM(String message) {
		this(null ,message, null,null);
	}
}
