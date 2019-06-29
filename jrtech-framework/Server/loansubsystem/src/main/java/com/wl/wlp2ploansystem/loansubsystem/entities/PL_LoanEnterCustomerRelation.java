package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
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
@TableName("pl_loan_enter_customerrelation")
public class PL_LoanEnterCustomerRelation extends FullAuditedEntity {

    @Display("申请单")
    @NotBlank
    @Size(max = 50)
    private String loanDocId;

    @Display("关联人类型")
    @Size(max = 50)
    private String categoryId;

    @Display("关联人名称")
    @Size(max = 50)
    private String name;

    @Display("联系电话")
    @Size(max = 50)
    private String tel;

    @Display("联系住址")
    @Size(max = 200)
    private String houseAddress;

    @Display("身份证号")
    @Size(max = 50)
    private String personalCardNO;

    @Display("性别")
    @NotBlank
    private String personalSexId;

    @Display("年龄")
    @NotBlank
    private Integer personalAge;

    @Display("婚姻状况")
    @NotBlank
    @Size(max = 50)
    private String personalMaritalStatusId;

    @Display("学历")
    @Size(max = 50)
    private String personalEducationalLevel;

    @Display("工作单位")
    @Size(max = 200)
    private String personalCompanyName;

    @Display("职务")
    @Size(max = 50)
    private String personalJobId;

    @Display("营业执照")
    @NotBlank
    @Size(max = 50)
    private String companyBusinessLicence;

    @Display("注册资金(万)")
    @NotBlank
    private BigDecimal companyRegistrationCapital;

    @Display("注册时间")
    @NotBlank
    private LocalDateTime companyRegistrationDate;

    @Display("所属行业")
    @NotBlank
    @Size(max = 50)
    private String companyBusinessType;

    @Display("法人姓名")
    @NotBlank
    @Size(max = 50)
    private String companyLegalPersonName;

    @Display("联系电话")
    @NotBlank
    @Size(max = 50)
    private String companyContactorMobile;

    @Display("股东情况")
    @Size(max = 200)
    private String companyStockholderInfo;

    @Display("关联关系")
    @Size(max = 50)
    private String relationCategoryId;



}