package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Base_Role extends FullAuditedEntity {

	public static  final  String Rolecategory_trackingpersoninfomr = "base_rolecategory_trackingpersoninfomr";

	private String organizationId;
	private String name; 
	private String remark;
	private Integer type = 1;
	private  String roleCategoryId;
	@TableField(exist = false)
	private String roleCategoryName;

	public  boolean canDelete(){
		return  type != 0; //0 systemRole
	}
}
