package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.support.ITreeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_navigation")
public class Base_Navigation extends FullAuditedEntity implements ITreeObject {
	
	@Display("父编号")
	public String parentId;
	
	private String name;
	
	private String title;
	
	private String path;
	
	private String icon;
	 
	private String component;
	

	private String requiredPermissionId;

	//0,不显示，1.显示，2：不显示在菜单，显示在tabNav上
	private Boolean show;

	private  Boolean keepAlive;

	//0，显示侧边栏,1:不显示侧边栏
	private  Integer layoutType = 0;

	private String remark;

	private  Integer sortIndex;

	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "Base_Navigation";
	}
}
