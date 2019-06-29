package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/***
 * 任务委派输入 dto
 */
@Data
public class WF_DelegateInDto {
    /**
     * 任务Id
     */
    @NotBlank
    private String taskId;
    /***
     * 委派人
     */
    @NotBlank
    private String userId;
    /***
     * 备注
     */
    private String remark;
}
