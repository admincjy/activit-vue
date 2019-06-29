package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Notification;
import lombok.Data;

@Data
public class Base_NotificationPublishOrNotInputDto {
    Base_Notification input;
    Boolean published;
}
