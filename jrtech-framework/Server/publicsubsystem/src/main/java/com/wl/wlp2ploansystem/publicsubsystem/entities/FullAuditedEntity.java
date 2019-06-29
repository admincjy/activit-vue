package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString 
public class FullAuditedEntity extends Entity {


	@TableField(fill = FieldFill.INSERT)
	@Display("创建人")
	private String gmtCreatedBy;
	@TableField(fill = FieldFill.INSERT)
	
	@Display("创建日期")
	private LocalDateTime gmtCreatedOn;

	@TableField(fill = FieldFill.UPDATE)
	@Display("修改人") 
	private String gmtUpdatedBy;
	
	@TableField(fill = FieldFill.UPDATE)
	@Display("修改日期")
	private LocalDateTime gmtUpdatedOn; 
	
	@Version
	@Display("锁版本")
	private Integer gmtVersion = 0;

	public  void resetFileds(){
		this.setId(null);
		this.gmtCreatedBy = null;
		this.gmtCreatedOn = null;
		this.gmtUpdatedBy = null;
		this.gmtUpdatedOn = null;
		this.gmtVersion = 0;
	}
}
