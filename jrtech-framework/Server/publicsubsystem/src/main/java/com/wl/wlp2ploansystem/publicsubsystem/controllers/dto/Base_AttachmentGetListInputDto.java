package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Base_AttachmentGetListInputDto extends CommonCategoryIdAndSortingDto {
    private   String businessDocId;
}
