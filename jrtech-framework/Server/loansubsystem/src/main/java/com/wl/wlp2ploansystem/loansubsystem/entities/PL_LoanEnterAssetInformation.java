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
@TableName("pl_loan_enter_asset_information")
public class PL_LoanEnterAssetInformation extends FullAuditedEntity {

	@Display("申请单")
	@NotBlank
	@Size(max = 50)
	private String loanDocId;

	@Display("资产类型")
	@NotBlank
	@Size(max = 50)
	private String categoryId;

	@Display("所有权人")
	@NotBlank
	@Size(max = 50)
	private String owner;

	@Display("权属号")
	@NotBlank
	@Size(max = 50)
	private String code;

	@Display("价值(万)")
	@NotBlank
	private BigDecimal value;

	@Display("资产现状")
	@NotBlank
	@Size(max = 100)
	private String nowStatus;

	@Display("购买时间")
	@NotBlank
	private LocalDateTime buyDate;

}