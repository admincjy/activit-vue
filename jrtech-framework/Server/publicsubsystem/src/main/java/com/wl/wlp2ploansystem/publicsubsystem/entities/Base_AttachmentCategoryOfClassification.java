package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_attachment_category_of_classification")
public class Base_AttachmentCategoryOfClassification extends Entity {

    private String categoryId;
    private String categoryClassificationId;
    private Boolean required;


}
