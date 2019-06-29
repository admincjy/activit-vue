package com.wl.wlp2ploansystem.infrastructures.common.aop;

import java.time.LocalDateTime;

/**
* 系统日志记录内容
* */
public class LoggingDetails {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户操作(Log注解内容）{@link com.wl.wlp2ploansystem.infrastructures.common.annotations.Log}
     */
    private String operation;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 执行结果 error:失败 success:成功
     */
    private String result;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 错误堆栈
     */
    private String errorStackTrace;
    /**
     * 执行时间
     */
    private LocalDateTime executeTime;
    /**
     * 执行时长(毫秒)
     */
    private Long duration;
    /**
     * IP地址
     */
    private String ip;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }



}
