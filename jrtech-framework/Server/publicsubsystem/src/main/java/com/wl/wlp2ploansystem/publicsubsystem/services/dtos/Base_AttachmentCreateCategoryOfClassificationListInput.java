package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategoryOfClassification;
import lombok.Data;

import java.util.Collection;

@Data
public class Base_AttachmentCreateCategoryOfClassificationListInput {

    private Collection<Base_AttachmentCategoryOfClassification>  categoryOfClassificationList;

    private  String categoryClassificationId;
}
