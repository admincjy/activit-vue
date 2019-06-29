package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_v_processinst_full")
public class WF_ProcessInstFull extends WF_ProcessInst {

    @Display("流程名称")
    private String processDefinationName;

    @Display("提单人")
    private String origiatorName;

    private  String taskId;
    private  String taskActId;
    private  String taskActName;
    private  String taskAssignee;
    private  String taskAssigneeName;
    private  String taskCandidate;
    private  String taskCandidateName;
    private LocalDateTime taskCreateDate;
    private String taskFormKey;

    public String getTaskActUser() {

        return  StringUtils.isEmpty(taskAssignee)?taskCandidate:taskAssignee;

    }
    public String getTaskActUserName() {

        return  StringUtils.isEmpty(taskAssigneeName)?taskCandidateName:taskAssigneeName;

    }
    public String getTaskFormUrl(){

        if(StringUtils.isEmpty(this.getTaskFormKey())){
            return  null;
        }
        return MessageFormat.format("{0}?taskId={1}&id={2}&procInstId={3}",
                this.getTaskFormKey(),
                this.getTaskId(),
                this.getBusinessId(),
                this.getId());
    }
}
