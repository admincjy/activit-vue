package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_attachment_category")
public class Base_AttachmentCategory extends FullAuditedEntity {
    @Display("名称")
    @NotBlank
    private String name;
    private String remark;
    private boolean visible;

    private Integer sortIndex;

    @TableField(exist = false)
    private Boolean required;
    @TableField(exist = false)
    private  String categoryClassificationId;
}