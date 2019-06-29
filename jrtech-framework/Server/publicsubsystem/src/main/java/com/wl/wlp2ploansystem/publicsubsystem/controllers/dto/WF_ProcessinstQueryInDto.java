package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class WF_ProcessinstQueryInDto  extends CommonPagedInputDto {

    /***
     * 流程类别
     */
    private String processDefinationKey;
    /***
     * 流程发起时间-开始
     */
    private LocalDateTime startDateBegin;
    /***
     * 流程发起时间-结束
     */
    private LocalDateTime startDateEnd;


}
