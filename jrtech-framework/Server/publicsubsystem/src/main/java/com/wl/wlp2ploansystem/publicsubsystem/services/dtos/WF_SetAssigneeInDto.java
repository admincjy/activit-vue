package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/***
 * 任务转办输入 dto
 */
@Data
public class WF_SetAssigneeInDto {
    /**
     * 任务Id
     */
    @NotBlank
    private String taskId;
    /***
     * 转办到人
     */
    @NotBlank
    private String assignee;
    /***
     * 备注
     */
    private String remark;
}
