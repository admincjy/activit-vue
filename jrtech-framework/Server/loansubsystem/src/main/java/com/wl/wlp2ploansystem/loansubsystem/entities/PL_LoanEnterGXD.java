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
@TableName("pl_loan_enter_gxd")
public class PL_LoanEnterGXD extends FullAuditedEntity {

    @Display("申请编号")
    @NotBlank
    @Size(max = 50)
    private String customerCode;

    @Display("客户名称")
    @NotBlank
    @Size(max = 50)
    private String customerName;

    @Display("身份证号")
    @NotBlank
    @Size(max = 50)
    private String customerCardNO;

    @Display("联系方式")
    @NotBlank
    @Size(max = 40)
    private String mobile;

    @Display("性别")
    @NotBlank
    private String sexId;

    @Display("年龄")
    @NotBlank
    private Integer age;

    @Display("婚姻状况")
    @NotBlank
    @Size(max = 50)
    private String maritalStatusId;

    @Display("学历")
    @NotBlank
    @Size(max = 50)
    private String educationalLevel;

    @Display("工作单位")
    @Size(max = 200)
    private String companyName;

    @Display("职务")
    @NotBlank
    @Size(max = 50)
    private String jobId;

    @Display("从业年限")
    @NotBlank
    @Size(max = 50)
    private String companyWorkTerm;

    @Display("社保编号")
    @Size(max = 50)
    private String socialSecurityCode;

    @Display("居住情况")
    @NotBlank
    @Size(max = 50)
    private String residentialStatusId;

    @Display("联系地址")
    @NotBlank
    @Size(max = 100)
    private String residentialAddress;

    @Display("户籍地址")
    @NotBlank
    @Size(max = 100)
    private String placeOfHouseholdRegistration;

    @Display("业务来源")
    @NotBlank
    @Size(max = 50)
    private String businessSourceId;

    @Display("客户经理")
    @Size(max = 50)
    private String trackingPersonInfoMRId;

    @Display("紧急联系人")
    @NotBlank
    @Size(max = 50)
    private String emergencyContactor;

    @Display("紧急联系人关系")
    @NotBlank
    @Size(max = 50)
    private String emergencyContactorRelation;

    @Display("紧急联系人关系电话")
    @NotBlank
    @Size(max = 50)
    private String emergencyContactorMobile;

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

    @Display("家庭月收入(元)")
    @NotBlank
    private BigDecimal financeFamilyMonthIncome;

    @Display("家庭月支出(元)")
    @NotBlank
    private BigDecimal financeFamilyMonthPay;

    @Display("家庭总资产(万)")
    @NotBlank
    private BigDecimal financeFamilyTotalAsset;

    @Display("房产套数")
    @NotBlank
    private Integer financeHouseCount;

    @Display("还款负担率")
    @NotBlank
    private BigDecimal financeReturnMoneyLoanRate;

    @Display("资产负债率")
    @NotBlank
    private BigDecimal financeAssetDebtRate;

}