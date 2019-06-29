package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PL_LoanEnterGXDFull extends PL_LoanEnterGXD{

    @Display("流程实例Id")
    @Size(max = 50)
    private String processInstId;

    @Display("流程单据状态")
    @Size(max = 50)
    private String processInstStatus;

    @Display("产品类型")
    private String loanProductTypePath;

    @Display("客户经理")
    private String trackingPersonInfoMRName;

    @Display("单据所属用户")
    @Size(max = 50)
    private String docOwnUserId;

    @Display("单据所属用户")
    private String docOwnUserName;

    @Display("单据所属小组")
    @Size(max = 50)
    private String docOwnGroupId;

    @Display("单据所属小组")
    private String docOwnGroupName;

    @Display("所属营业部")
    @Size(max = 50)
    private String docOwnDepartmentId;

    @Display("所属营业部")
    private String docOwnDepartmentName;

    @Display("表单模板")
    @NotBlank
    private String  formTemplateTypeId;

    @Display("资产信息")
    @NotBlank
    private List<PL_LoanEnterAssetInformation> assetInformationList;

    @Display("评估价格")
    private BigDecimal carEvalueMoneyAmount;

    @Display("审批金额")
    private BigDecimal reAuditLoanMoneyAmount;

    @Display("审批期数")
    private Integer reAuditLoanTermCount;

}
