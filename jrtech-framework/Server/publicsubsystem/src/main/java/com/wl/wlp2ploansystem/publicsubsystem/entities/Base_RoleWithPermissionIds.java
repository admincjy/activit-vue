package com.wl.wlp2ploansystem.publicsubsystem.entities;
 
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

 
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Base_RoleWithPermissionIds extends Base_Role{

	@TableField(exist = false)
	private List<String> permissionIds = new ArrayList<String>();
	@TableField(exist = false)
	private List<String> dataPermissionIds = new ArrayList<String>();

}
