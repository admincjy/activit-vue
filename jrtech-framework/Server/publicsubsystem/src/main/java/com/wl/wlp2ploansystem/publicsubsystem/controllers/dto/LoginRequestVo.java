package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode()
@ApiModel(description = "登陆请求对象")
public class LoginRequestVo {

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty("登陆名")
    private String username;

    @NotNull
    @ApiModelProperty("登陆密码")
    private String password;
    @ApiModelProperty("验证码")
    private  String captchaCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaCode() {
        return this.captchaCode;
    }
    public void setCaptchaCode(String captchaCode){
        this.captchaCode = captchaCode;

    }

    @Override
    public String toString() {
        return "LoginVM{" +
                "username='" + username + '\'' +
                "password=*****" + '\'' +
                "captchaCode=" + captchaCode +
                '}';
    }
}
