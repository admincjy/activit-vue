package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Locale;



@TableName("base_user")
public class Base_User extends FullAuditedEntity {

	@Display("登陆账号")
	@NotBlank(message = "登陆账号不能为空")
	@Length(max = 20,message = "登陆账号不能超过20个字符")
	private String loginId;

	@Display("登陆密码")
	@NotBlank(message = "登陆密码不能为空")
	@Length(max = 20,message = "登陆密码不能超过20个字符")
	private String loginPassword;

	@Display("名称")
	@NotBlank(message = "名称不能为空")
	@Length(max = 20,message = "名称不能超过20个字符")
	private String name;

	@Display("联系方式")
	@Length(max = 50,message = "联系方式不能超过50个字符")
	private String mobile;

	@Display("邮箱")
	@Length(max = 30,message = "邮箱不能超过30个字符")
	private String email;

	@Display("出生日期")
	private LocalDateTime birthday;

	@Display("备注")
	@Length(max = 200,message = "备注不能超过200个字符")
	private String remark;
	
	@Display("是否可用")
	private Boolean activited;

	@Display("最后登陆日期")
	private LocalDateTime lastLoginTime;

	@Display("用户登陆时是否应该修改密码（尚未实现功能)")
	private Boolean shouldChangePassword = true;


	//逻辑删除 0：未删除，1：已删除
	@TableField(value = "deleted")
	@TableLogic
	private Integer deleted = 0;

	@JsonIgnore
	public boolean  isSystemAdmin(){
		return this.loginId.equalsIgnoreCase("admin");
	}
	@JsonIgnore
	public  boolean canDelete(){
		return  !this.isSystemAdmin();
	}

	//^ 匹配一行的开头位置
	//(?![0 - 9] +$) 预测该位置后面不全是数字
	//(?![a - zA - Z] +$) 预测该位置后面不全是字母
	//(?!\1{5}) 至少不能5位相同
	//[0 - 9A - Za - z] { 8,16}
	//由8 - 16位数字或这字母组成
	//$ 匹配行结尾位置
	public static final String userUasswordCheckRegexPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)(?!(.)\\1{5}).{8,16}$";

	public String getLowerCaseLoginId(){
		if(StringUtils.isEmpty((this.loginId))){
			return  null;
		}
		return  this.loginId.toLowerCase(Locale.ENGLISH);
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	@JsonIgnore
	public String getLoginPassword() {
		return loginPassword;
	}
	@JsonProperty
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

	public LocalDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDateTime birthday) {
		this.birthday = birthday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getActivited() {
		return activited;
	}

	public void setActivited(Boolean activited) {
		this.activited = activited;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Boolean getShouldChangePassword() {
		return shouldChangePassword;
	}

	public void setShouldChangePassword(Boolean shouldChangePassword) {
		this.shouldChangePassword = shouldChangePassword;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
