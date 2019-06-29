package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

import java.util.List;

@Data
public class Base_AttachmentClassificationIdsWithBusinessDocIdDto {
    private  List<String> cagegoryClassificationIds;
    private String businessDocId;
}
