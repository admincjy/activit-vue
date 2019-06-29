package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class Base_UserWithAuthorities  extends Base_User{

	@TableField(exist = false)
	private List<String> grantedAuthorities;

	@TableField(exist = false)
	private List<String> dataPermissionIds;

	@TableField(exist = false)
	private List<String> roleIds;

	@TableField(exist = false)
	private List<String> groupIds;

	@TableField(exist = false)
	private List<String> departmentIds;

	public UserDetails toUserDetail(){

		List<DomainUserDetails.DomainGrantedAuthority> grantedAuthorities = this.getGrantedAuthorities().stream()
				.map(authority -> new DomainUserDetails.DomainGrantedAuthority(authority)).collect(Collectors.toList());

		DomainUserDetails domainUser = new DomainUserDetails();
		domainUser.setId(this.getId());
		domainUser.setLoginId(this.getLoginId());
		domainUser.setLoginPassword(this.getLoginPassword());
		domainUser.setName(this.getName());
		domainUser.setEmail(this.getEmail());
		domainUser.setMobile(this.getMobile());
		domainUser.setActivited(this.getActivited());
		domainUser.setShouldChangePassword(this.getShouldChangePassword());
		domainUser.setAuthorities(grantedAuthorities);
		domainUser.setRoleIds(this.getRoleIds());
		domainUser.setGroupIds(this.getGroupIds());
		domainUser.setDepartmentIds(this.getDepartmentIds());
		domainUser.setDataPermissionIds(this.getDataPermissionIds());

		return  domainUser;

	}

}
