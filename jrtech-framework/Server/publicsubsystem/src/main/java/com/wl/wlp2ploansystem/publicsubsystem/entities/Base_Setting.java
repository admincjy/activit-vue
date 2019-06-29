package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_setting")
public class Base_Setting extends FullAuditedEntity {
    @Display("值")
    @NotBlank
    @Length(max = 2000)
    private String value;

    @Display("名称")
    @NotBlank
    @Length(max = 50)
    private String name;

    @Display("备注")
    @Length(max = 200)
    private String remark;

}
