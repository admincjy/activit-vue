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
@TableName("wf_v_processinsthistory")
public class WF_ProcessInstHistoryFull extends Entity {

    @Display("ProcessInstId")
    @NotBlank
    @Size(max = 50)
    private String processInstId;

    @Display("ActId")
    @NotBlank
    @Size(max = 50)
    private String actId;

    @Display("ActName")
    @NotBlank
    @Size(max = 50)
    private String actName;

    @Display("办理人")
    @NotBlank
    @Size(max = 50)
    private String actUserId;


    private String actUserName;


    private String doAction;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    private String result;


    private String remark;
}