package com.wl.wlp2ploansystem.infrastructures.common.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserFriendlyException extends RuntimeException {
	public final static int  STATUS_CODE = 8888;
	private Integer statusCode = 8888;

	/***
	 * 错误编码
	 */
	private String code;
	/***
	 * 错误信息
	 */
	private String message;
	/***
	 * 详细错误信息
	 */
	private String details;
	/***
	 * 内部错误
	 */
	private Exception exception;

	/***
	 * 额外参数
	 */
	private Object payload;

	public UserFriendlyException(){

	}
	public UserFriendlyException( String code, String message, String details,Exception exception,Object payload) {
		this.code = code;
		this.message = message;
		this.details = details;
		this.exception=exception;
		this.payload = payload;
	}
	public UserFriendlyException( String message, String details,Exception exception,Object payload) {
		this(null, message, details,exception,payload);
	}
	public UserFriendlyException( String message, String details,Exception exception) {
		this(null, message, details,exception,null);
	}
	public UserFriendlyException(String message, String details) {
		this(null, message, details,null,null);
	}
	public UserFriendlyException(String message, Exception exception) {
		this(null, message,null, exception,null);
	}
	public UserFriendlyException(String message) {
		this(null, message,null, null,null);
	}


}
