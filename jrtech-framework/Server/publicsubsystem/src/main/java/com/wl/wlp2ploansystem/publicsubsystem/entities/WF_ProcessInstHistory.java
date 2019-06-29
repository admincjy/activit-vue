package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_processinsthistory")
public class WF_ProcessInstHistory extends FullAuditedEntity {

    @Display("ProcessInstId")
    @NotBlank
    @Size(max = 50)
    private String processInstId;

    @Display("taskId")
    @NotBlank
    @Size(max = 50)
    private String taskId;

    @Display("ActId")
    @NotBlank
    @Size(max = 50)
    private String actId;

    @Display("ActName")
    @NotBlank
    @Size(max = 50)
    private String actName;

    /***
     * 办理人来源(asignee:办理 :,delegate:委派 ,redirect:转办，claim:签收)
     */
    @NotBlank
    @Size(max = 50)
    private  String actUserSource;

    @Display("办理人")
    @NotBlank
    @Size(max = 50)
    private String actUserId;


    @Display("DoAction")
    @NotBlank
    @Size(max = 50)
    private String doAction;

    @Display("actionDate")
    @NotBlank
    private LocalDateTime actionDate;

    @Display("result")
    @NotBlank
    @Size(max = 50)
    private String result;

    @Display("Remark")
    @Size(max = 500)
    private String remark;

}