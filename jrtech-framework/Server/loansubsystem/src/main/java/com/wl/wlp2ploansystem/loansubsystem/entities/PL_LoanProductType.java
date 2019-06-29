package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.publicsubsystem.entities.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pl_loan_product_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PL_LoanProductType extends FullAuditedEntity {
    @Display("名称")
    @NotBlank
    @Size(max = 200)
    private String name;

    @Display("描述")
    @Size(max = 400)
    private String remark;
}
