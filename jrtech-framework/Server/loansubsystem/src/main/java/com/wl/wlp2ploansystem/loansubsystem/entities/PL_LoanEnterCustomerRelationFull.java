package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class PL_LoanEnterCustomerRelationFull extends PL_LoanEnterCustomerRelation{

    @Display("关联人类型")
    @Size(max = 50)
    @TableField(exist = false)
    private String categoryName;

    @Display("关联关系")
    @Size(max = 50)
    private String relationCategoryName;
}