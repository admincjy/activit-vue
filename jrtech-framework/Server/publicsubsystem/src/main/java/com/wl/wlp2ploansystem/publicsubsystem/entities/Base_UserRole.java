package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_user_role")
public class Base_UserRole extends Entity {

	private String userId;
	private String roleId;
	
}
