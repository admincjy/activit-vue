package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode()
@ApiModel(description = "登陆返回结果")
public class LoginResponseVo {
    public String getAppKey() {
        return appKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
    public LoginResponseVo setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
        return this;
    }

    public Boolean getUserShouldChangePassword() {
        return userShouldChangePassword;
    }

    public void setUserShouldChangePassword(Boolean userShouldChangePassword) {
        this.userShouldChangePassword = userShouldChangePassword;
    }

    @ApiModelProperty("用户token")
    private  String appKey;
    @ApiModelProperty("用户Id")
    private  String userId;

    @ApiModelProperty("用户登陆账号")
    private  String userLoginId;
    @ApiModelProperty("用户名称")
    private  String userDisplayName;
    @ApiModelProperty("用户手机号码")
    private  String userMobile;

    @ApiModelProperty("用户是否首次登陆需要修改密码")
    private  Boolean userShouldChangePassword;

}
