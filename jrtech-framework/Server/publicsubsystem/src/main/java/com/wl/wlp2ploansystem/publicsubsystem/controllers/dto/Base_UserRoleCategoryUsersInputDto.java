package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Base_UserRoleCategoryUsersInputDto extends CommonPagedInputDto {
    private String roleCategoryId;
}
