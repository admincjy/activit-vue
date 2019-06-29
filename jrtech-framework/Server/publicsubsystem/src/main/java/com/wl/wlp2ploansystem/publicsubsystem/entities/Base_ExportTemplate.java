 package com.wl.wlp2ploansystem.publicsubsystem.entities;

 import com.baomidou.mybatisplus.annotations.TableName;
 import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
 import lombok.Data;
 import lombok.EqualsAndHashCode;

 import javax.validation.constraints.Size;

 @EqualsAndHashCode(callSuper = true)
 @Data
@TableName("base_export_template")
public class Base_ExportTemplate extends FullAuditedEntity {

    @Display("名称")
    @Size(max = 200)
    private String name;

    @Display("模板路径")
    @Size(max = 200)
    private String path;

    @Display("模板类别")
    @Size(max = 200)
    private String templateCategoryId;

    @Display("Remark")
    @Size(max = 500)
    private String remark;

}