package com.wl.wlp2ploansystem.infrastructures.workflow.vo;

import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode()
public class WF_ProcessInstGotoDto {

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
    public Map<String, Object> getDataFields() {
        return dataFields;
    }

    public void setDataFields(Map<String, Object> dataFields) {
        this.dataFields = dataFields;
    }
    public String getTargetTaskDefinitionKey() {
        return targetTaskDefinitionKey;
    }

    public void setTargetTaskDefinitionKey(String targetTaskDefinitionKey) {
        this.targetTaskDefinitionKey = targetTaskDefinitionKey;
    }

    public String getProcessInstId() {
        return processInstId;
    }

    public WF_ProcessInstGotoDto setProcessInstId(String processInstId) {
        this.processInstId = processInstId;
        return this;
    }

    private String processInstId;
    private String action;
    private String taskRemark;
    private String docId;

    private Map<String, Object> dataFields;

    private String targetTaskDefinitionKey;
}
