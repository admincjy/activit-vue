package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.publicsubsystem.entities.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pl_loan_enter_company")
public class PL_LoanEnterCompany extends FullAuditedEntity {

    @Display("申请编号")
    @NotBlank
    @Size(max = 50)
    private String customerCode;

    @Display("借款企业名称")
    @NotBlank
    @Size(max = 100)
    private String companyName;

    @Display("注册资金(万)")
    @NotBlank
    private BigDecimal companyRegistrationCapital;

    @Display("所属行业")
    @NotBlank
    @Size(max = 50)
    private String companyBusinessType;

    @Display("成立年限(年)")
    @NotBlank
    private Integer companyBuildYears;

    @Display("联系地址")
    @NotBlank
    @Size(max = 200)
    private String companyWorkAddress;

    @Display("所在区域")
    @NotBlank
    @Size(max = 50)
    private String companyOfArea;

    @Display("股东名称")
    @NotBlank
    @Size(max = 50)
    private String stockholderNames;

    @Display("关联企业")
    @NotBlank
    @Size(max = 200)
    private String affiliatedEnterprise;

    @Display("员工人数(个)")
    @NotBlank
    private Integer companyEmpoyeeCount;

    @Display("联系人")
    @NotBlank
    @Size(max = 50)
    private String emergencyContactor;

    @Display("联系电话")
    @NotBlank
    @Size(max = 50)
    private String emergencyContactorMobile;

    @Display("业务来源")
    @NotBlank
    @Size(max = 50)
    private String businessSource;

    @Display("客户经理")
    @Size(max = 50)
    private String trackingPersonInfoMRId;

    @Display("住房类固定资产总套数")
    @NotBlank
    @Size(max = 50)
    private String assetInformationHouseCount;

    @Display("申请金额")
    private BigDecimal loanMoneyAmount;

    @Display("申请期限")
    private Integer loanTermCount;

    @Display("还款方式")
    @NotBlank
    @Size(max = 50)
    private String returnMoneyMethodId;

    @Display("借款用途")
    @NotBlank
    @Size(max = 50)
    private String loanRegPurpose;

    @Display("还款来源")
    @NotBlank
    @Size(max = 50)
    private String returnMoneySource;

    @Display("资金来源")
    @NotBlank
    @Size(max = 50)
    private String fundsSourceId;

    @Display("放款开户行")
    @NotBlank
    @Size(max = 100)
    private String bankOfOpen;

    @Display("放款账号")
    @NotBlank
    @Size(max = 50)
    private String bankAccountNo;

    @Display("户名")
    @NotBlank
    @Size(max = 50)
    private String bankAccounName;

    @Display("借款产品")
    @NotBlank
    @Size(max = 50)
    private String loanProductSubTypeId;

    @Display("申请日期")
    private LocalDateTime loanApplyDate;

    @Display("备注")
    @Size(max = 400)
    private String remark;

    @Display("年营业额(万)")
    @NotBlank
    private BigDecimal financeYearSalesVolume;

    @Display("总资产(万)")
    @NotBlank
    private BigDecimal financeTotalAsset;

    @Display("资产负债率")
    @NotBlank
    private BigDecimal financeTotalAssetDebtRate;

    @Display("流动比")
    @NotBlank
    private BigDecimal financeFlowRate;

    @Display("速动比")
    @NotBlank
    private BigDecimal financeQuickRate;

    @Display("上年度纳税申请额(万)")
    @NotBlank
    private BigDecimal financeTaxAmount;

    @Display("利润率")
    @NotBlank
    private BigDecimal financeProfitRate;

    @Display("近半年银行流水情况")
    @Size(max = 200)
    private String financeBankStatement;

}