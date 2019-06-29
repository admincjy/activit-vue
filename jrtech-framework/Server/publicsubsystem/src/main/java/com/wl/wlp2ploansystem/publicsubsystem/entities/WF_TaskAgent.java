package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * 任务授权
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_task_agent")
public class WF_TaskAgent extends FullAuditedEntity {

    public static final String WF_TaskAgent_Status_Enabled="enabled";
    public static final String WF_TaskAgent_Status_Disabled="disabled";
    public static final String WF_TaskAgent_Status_Overdued="overdued";
    /**
     * 授权人
     */
    @NotBlank
    @Size(max = 50)
    private String assignee;
    /**
     * 被授权人
     */
    @NotBlank
    @Size(max = 50)
    private String attorney;
    /**
     * 开始日期
     */
    @NotNull
    private LocalDateTime startTime;
    /**
     * 结束日期
     */
    @NotNull
    private LocalDateTime endTime;
    /**
     * 流程定义
     */
 
    @Size(max = 50)
    private String processDefinitionKey;

 
    /**
     * 状态
     */
    @Size(max = 50)
    private String status;
}
