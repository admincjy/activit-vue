package com.wl.wlp2ploansystem.publicsubsystem.entities;
 
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

 
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Base_UserWithRoleIds extends Base_User{

	@TableField(exist = false)
	private List<String> roleIds = new ArrayList<String>();
}
