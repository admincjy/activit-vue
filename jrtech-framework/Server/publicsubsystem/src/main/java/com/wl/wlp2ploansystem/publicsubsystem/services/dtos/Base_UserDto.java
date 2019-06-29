package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public class Base_UserDto {

	@Display("登陆账号")
	@NotBlank
	@Length(max = 20)
	private String loginId;

	@Display("名称")
	@NotBlank
	@Length(max = 20)
	private String name;

	@Display("联系方式")
	@Length(max = 50)
	private String mobile;

	@Display("邮箱")
	@Length(max = 30)
	private String email;

	@Display("出生日期")
	private LocalDateTime birthday;

	@Display("备注")
	@Length(max = 200)
	private String remark;
	
	@Display("是否可用")
	private boolean activited;
	
}
