package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_attachment")
public class Base_Attachment extends FullAuditedEntity {

    @Display("名称")
    @NotBlank
    private String name;
    private String path;
    private Long fileSize;
    private Integer downloadCount;
    private String fileType;
    private String businessDocId;
    private String attachmentCategoryId;
    private String remark;
}
