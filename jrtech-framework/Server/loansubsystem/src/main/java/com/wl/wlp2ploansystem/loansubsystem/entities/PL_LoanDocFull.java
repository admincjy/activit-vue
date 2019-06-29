package com.wl.wlp2ploansystem.loansubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pl_v_loan_doc_full")
public class PL_LoanDocFull extends PL_LoanDoc{

    @Display("流程实例Id")
    @Size(max = 50)
    private String processInstId;

    @Display("流程单据状态")
    @Size(max = 50)
    private String processInstStatus;

    @Display("产品类型")
    private String loanProductTypePath;

    @Display("开发人")
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

    @Display("单据所属小组路径")
    @Size(max = 50)
    private String docOwnGroupIdPath;
}
