package com.wl.wlp2ploansystem.infrastructures.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Spring Security UserDetails实现类
 *
 */
public class DomainUserDetails  implements UserDetails{

	public  static  class DomainGrantedAuthority implements GrantedAuthority {
		@Override
		public String getAuthority() {
			return authority;
		}

		public void setAuthority(String authority) {
			this.authority = authority;
		}

		private String authority;

		public DomainGrantedAuthority(String authority) {
			this.authority = authority;
		}
		public DomainGrantedAuthority() {
		}
	}
	@Display("用户id")
	private String id;

	@Display("登陆账号")
	@NotBlank
	@Length(max = 20)
	private String loginId;

	@Display("登陆密码")
	@NotBlank
	@Length(max = 20)
	private String loginPassword;

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

	@Display("是否可用")
	private Boolean activited;

	@Display("是否应修改密码")
	private Boolean shouldChangePassword = Boolean.TRUE;
	/**
	 * 数据权限
	 */
	private Collection<String> dataPermissionIds;

	/**
	 * 所属角色
	 */
	private Collection<String> roleIds;
	/**
	 * 所属组织
	 */
	private Collection<String> groupIds;
	/**
	 * 所属营业部
	 */
	private Collection<String> departmentIds;
	/**
	 * 功能权限
	 */
	private Collection<DomainGrantedAuthority> authorities;

	/**
	 * 是否系统管理员
	 * @return
	 */
	@JsonIgnore
	public boolean  isSystemAdmin(){
		return this.loginId.equalsIgnoreCase("admin");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActivited() {
		return activited;
	}

	public void setActivited(Boolean activited) {
		this.activited = activited;
	}

	public Boolean getShouldChangePassword() {
		return shouldChangePassword;
	}

	public void setShouldChangePassword(Boolean shouldChangePassword) {
		this.shouldChangePassword = shouldChangePassword;
	}

	public Collection<String> getDataPermissionIds() {
		return dataPermissionIds;

	}

	public void setDataPermissionIds(Collection<String> dataPermissionIds) {
		this.dataPermissionIds = dataPermissionIds;
	}

	public Collection<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Collection<String> roleIds) {
		this.roleIds= roleIds;
	}

	public Collection<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(Collection<String> groupIds) {
		this.groupIds =groupIds;
	}

	public Collection<String> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(Collection<String> departmentIds) {
		this.departmentIds = departmentIds;
	}


	public void setAuthorities(Collection<DomainGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return loginPassword;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return loginId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


}