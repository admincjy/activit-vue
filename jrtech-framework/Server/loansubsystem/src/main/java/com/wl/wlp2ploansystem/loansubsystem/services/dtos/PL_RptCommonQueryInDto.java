package com.wl.wlp2ploansystem.loansubsystem.services.dtos;

import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class PL_RptCommonQueryInDto extends CommonPagedInputDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String organizationId;
    private String loanProductSubTypeId;
    private String docOwnUserId;

}
