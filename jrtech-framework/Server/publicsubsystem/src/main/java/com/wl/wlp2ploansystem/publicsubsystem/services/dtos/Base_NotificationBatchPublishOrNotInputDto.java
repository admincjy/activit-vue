package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

import java.util.List;

@Data
public class Base_NotificationBatchPublishOrNotInputDto {
    List<String> ids;
    Boolean published;
}
