package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import com.wl.wlp2ploansystem.infrastructures.common.support.SimpleTreeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Base_AttachmentCategoryTreeObject extends SimpleTreeObject {
    private  Boolean required;
    private Boolean checked;
    private Integer attachmentCount;
    public Base_AttachmentCategoryTreeObject(String id, String parentId, String name,Boolean required,Boolean checked, Integer attachmentCount,String nodeType) {
        super(id,parentId,name,nodeType);
        this.required = required;
        this.checked = checked;
        this.attachmentCount = attachmentCount;
    }
}
