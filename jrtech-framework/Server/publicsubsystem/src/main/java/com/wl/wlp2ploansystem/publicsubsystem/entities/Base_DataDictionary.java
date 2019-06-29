package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_data_dictionary")
public class Base_DataDictionary extends FullAuditedEntity {

    @Display("名称")
    @NotBlank
    private String name;

    private Integer sortIndex;

    private String remark;

    private String dataDictionaryCategoryId;


    //逻辑删除 0：未删除，1：已删除
    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted = 0;

}
