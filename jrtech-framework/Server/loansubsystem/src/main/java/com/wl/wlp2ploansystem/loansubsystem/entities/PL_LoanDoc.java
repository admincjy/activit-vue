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
    @TableName("pl_loan_doc")
    public class PL_LoanDoc extends FullAuditedEntity {

        public  static final String loanApplyProcessDefinitionKey = "pl_loanapplydoc_process";

        //草稿
        public static final  String DocStatus_Draft = "draft";
        //进件审批
        public static final  String DocStatus_EnterApproving = "enterapproving";
        //回收
        public static final  String DocStatus_Recovery = "recovery";
        //出库
        public static final  String DocStatus_Settled = "settled";
        //拒绝
        public static final  String DocStatus_Declined = "declined";
        //坏账
        public static final  String DocStatus_badDebt = "badDebt";

        @Display("客户编码")
        @NotBlank
        @Size(max = 50)
        private String customerCode;

        @Display("客户名称")
        @NotBlank
        @Size(max = 50)
        private String customerName;

        @Display("身份证号")
        @Size(max = 50)
        private String customerCardNO;

        @Display("手机号码")
        @NotBlank
        @Size(max = 40)
        private String customerMobile;

        @Display("产品类别")
        @NotBlank
        @Size(max = 50)
        private String loanProductSubTypeId;

        @Display("开发人")
        @NotBlank
        @Size(max = 50)
        private String trackingPersonInfoMRId;

        @Display("客户经理")
        @NotBlank
        @Size(max = 50)
        private String customerMRId;

        @Display("借款金额")
        private BigDecimal loanMoneyAmount;

        @Display("借款期数")
        private Integer loanTermCount;

        @Display("申请金额")
        private BigDecimal originalLoanMoneyAmount;

        @Display("申请期数")
        private Integer originalLoanTermCount;
        @Display("评估价格")
        private BigDecimal carEvalueMoneyAmount;

        @Display("审批金额")
        private BigDecimal reAuditLoanMoneyAmount;

        @Display("审批期数")
        private Integer reAuditLoanTermCount;

        @Display("申请日期")

        private LocalDateTime loanApplyDate;

        @Display("提交日期")
        private LocalDateTime loanApplySumbitDate;

        @Display("还款算法")
        @Size(max = 50)
        private String returnMoneyMethodId;

        @Display("表单模板")
        @NotBlank
        private String formTemplateTypeId;

        @Display("合同编号")
        @Size(max = 50)
        private String contractCode;

        @Display("合同签定日期")
        private LocalDateTime contractSignDate;

        @Display("合同金额")
        private BigDecimal contractMoneyAmount;

        @Display("合同期数")
        private Integer contractTermCount;

        @Display("发放金额")
        private BigDecimal giveOutMoneyAmount;

        @Display("还款周期")
        private Integer contractRecoveryPeriodCount;

        @Display("还款日")
        private Integer contractReturnDay;

        @Display("其他费用")
        private BigDecimal contractOtherMoneyAmount;

        @Display("综合服务费")
        private BigDecimal contractServiceMoneyAmount;

        @Display("GPS费")
        private BigDecimal contractCarGPSMoneyAmount;

        @Display("保证金")
        private BigDecimal contractCarDpositMoneyAmount;

        @Display("停车费")
        private BigDecimal contractCarStopMoneyAmount;

        @Display("保险费")
        private BigDecimal contractCarInsuranceMoneyAmount;

        @Display("月其他费用")
        private BigDecimal contractMonthOtherMoneyAmount;

        @Display("月利率 ")
        private BigDecimal contractTaxFee;

        @Display("月管理费率")
        private BigDecimal contractMgrFee;

        @Display("手续费率")
        private BigDecimal contractHandlingFee;

        @Display("月服务费率")
        private BigDecimal contractServiceFee;

        @Display("其他费率")
        private BigDecimal contractOtherFee;

        @Display("滞纳金率")
        private BigDecimal contractOverdueFee;

        @Display("提前结清违约金率")
        private BigDecimal contractAheadOfTimeRecoveryClearedBreachOfContractFee;

        @Display("延期违约金率")
        private BigDecimal contractOverdueBreachOfContractFee;

        @Display("挂账金额")
        private BigDecimal  returnMoneyChargeUpAmount = BigDecimal.ZERO;


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

        @Display("发放日期")
        private LocalDateTime giveOutDate;

        @Display("单据状态")
        @Size(max = 50)
        private String docStatus;

        @Display("单据状态日期")
        private  LocalDateTime docStatusDate;

        @Display("单据状态原因")
        @Size(max = 200)
        private String docStatusReason;
    }