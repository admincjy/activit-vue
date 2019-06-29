package com.wl.wlp2ploansystem.infrastructures.workflow.vo;

import lombok.EqualsAndHashCode;

/**
 * 任务跳转传输对象
 */
@EqualsAndHashCode(callSuper = true)
public class WF_TaskGotoDto extends  WF_TaskActionDto {

    public String getTargetTaskDefinitionKey() {
        return targetTaskDefinitionKey;
    }

    public void setTargetTaskDefinitionKey(String targetTaskDefinitionKey) {
        this.targetTaskDefinitionKey = targetTaskDefinitionKey;
    }
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 跳转到结点
     */
    private String targetTaskDefinitionKey;
    /**
     * 跳转后办理结果
     */
    private String taskStatus;
}
