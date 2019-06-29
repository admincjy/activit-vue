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
@TableName("wf_processinst")
public class WF_ProcessInst extends FullAuditedEntity {
    @Display("processDefinationKey")
    @Size(max = 50)
    private String processDefinationKey;

    @Size(max = 50)
    private String businessId;

    @Display("Origiator")
    @NotBlank
    @Size(max = 50)
    private String origiator;

    @Display("saveDate")
    @NotBlank
    private LocalDateTime saveDate;

    @Display("StartDate")
    @NotBlank
    private LocalDateTime startDate;

    @Display("EndDate")
    private LocalDateTime endDate;

    @Display("Folio")
    @Size(max = 500)
    private String folio;

    /***
     * 记录挂起前的状态
     */
    @Display("初始状态")
    @Size(max = 50)
    private String originalStatus;

    @Display("当前状态")
    @Size(max = 50)
    private String status;

    @Display("Remark")
    @Size(max = 500)
    private String remark;

}