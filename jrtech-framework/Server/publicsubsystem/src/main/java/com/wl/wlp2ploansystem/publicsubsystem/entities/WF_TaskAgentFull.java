package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * 任务授权
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_task_agent_full")
public class WF_TaskAgentFull extends WF_TaskAgent {

    public String getShowStatus(){
        if(WF_TaskAgent.WF_TaskAgent_Status_Disabled.equals(this.getStatus())){
            return  WF_TaskAgent.WF_TaskAgent_Status_Disabled;
        }
        else{
            return  LocalDateTime.now().compareTo(this.getEndTime().plusDays(1))>=0?
                    WF_TaskAgent_Status_Overdued:
                    WF_TaskAgent_Status_Enabled;
        }
    }

    /**
     * 被授权人名称
     */
    @NotBlank
    @Size(max = 50)
    private String attorneyName;
    /**
     * 流程名称
     */
    @NotBlank
    @Size(max = 50)
    private String processDefinitionName;
}
