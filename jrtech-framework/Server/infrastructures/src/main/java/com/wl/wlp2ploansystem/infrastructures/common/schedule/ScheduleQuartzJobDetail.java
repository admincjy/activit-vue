package com.wl.wlp2ploansystem.infrastructures.common.schedule;

import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 定时任务日志记录对象
 *
 */
public class ScheduleQuartzJobDetail  implements Serializable {

    private static final long serialVersionUID = 1L;

    public   static   String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    public static final String JOB_RESULT_SUCCESS = "success";
    public static final String JOB_RESULT_EXECUTING= "executing";
    public static final String JOB_RESULT_ERROR = "error";
    public static final String JOB_STATUS_PAUSE = "pause";
    public static final String JOB_STATUS_NORMAL = "normal";


    @NotBlank(message="id不能为空")
    private String id;

    @NotBlank(message="名称不能为空")
    private String name;
    /**
     * spring bean名称
     */
    @NotBlank(message="bean名称不能为空")
    private String beanName;

    /**
     * 方法名
     */
    @NotBlank(message="方法名称不能为空")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    @NotBlank(message="cron表达式不能为空")
    private String cronExpression;

    /**
     * 任务状态
     */
    private String result;

    /**
     * 执行Id
     */

    private String executeId;

    //执行时长(毫秒)
    private Long duration;

    private LocalDateTime executeTime;

    private String errorMessage;

    private String errorStackTrace;



    private LocalDateTime gmtCreatedOn;
    private String gmtCreatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }


    public String getExecuteId() {
        return executeId;
    }

    public ScheduleQuartzJobDetail setExecuteId(String executeId) {
        this.executeId = executeId;
        return this;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
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

    public LocalDateTime getGmtCreatedOn() {
        return gmtCreatedOn;
    }

    public ScheduleQuartzJobDetail setGmtCreatedOn(LocalDateTime gmtCreatedOn) {
        this.gmtCreatedOn = gmtCreatedOn;
        return this;
    }

    public String getGmtCreatedBy() {
        return gmtCreatedBy;
    }

    public ScheduleQuartzJobDetail setGmtCreatedBy(String gmtCreatedBy) {
        this.gmtCreatedBy = gmtCreatedBy;
        return this;
    }
}
