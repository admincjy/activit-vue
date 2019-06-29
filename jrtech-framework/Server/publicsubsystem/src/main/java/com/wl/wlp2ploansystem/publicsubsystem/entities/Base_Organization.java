package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.support.ITreeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_organization")
public class Base_Organization extends FullAuditedEntity implements ITreeObject {

	@Display("父编号")
	@NotBlank
	private String parentId;

	@Display("名称")
	@NotBlank
	private String name;

	@Display("地址")
	private String address;

	@Display("邮编")
	private String postNO;

	private String remark;
	
	private Integer type = 1;

	private  String organizationCategoryId;
	@TableField(exist = false)
	private  String  organizationCategoryName;

	public  boolean canDelete(){
		return  type != 0; //0 systemOrg
	}
	@Override
	public String getNodeType() {
		return "Base_Organization_Entity";
	}

}
