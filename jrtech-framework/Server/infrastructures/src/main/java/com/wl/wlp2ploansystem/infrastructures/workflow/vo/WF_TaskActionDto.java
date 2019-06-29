package com.wl.wlp2ploansystem.infrastructures.workflow.vo;

import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 待办任务处理传输对象
 */
@EqualsAndHashCode()
public class WF_TaskActionDto {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 是否签收审批动作
     */
    private Boolean isClaim = false;
    /**
     * 审批动作
     */
    private String action;


    /**
     * 操作结果
     */
    private String result;
    /**
     * 审批意见
     */
    private String taskRemark;
    /**
     * 业务单据Id
     */
    private String docId;
    /**
     * 任务工作流变量
     */
    private Map<String, Object> dataFields;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getDataFields() {
        return dataFields;
    }

    public void setDataFields(Map<String, Object> dataFields) {
        this.dataFields = dataFields;
    }
    public Boolean getClaim() {
        return isClaim;
    }

    public void setClaim(Boolean claim) {
        isClaim = claim;
    }
}
