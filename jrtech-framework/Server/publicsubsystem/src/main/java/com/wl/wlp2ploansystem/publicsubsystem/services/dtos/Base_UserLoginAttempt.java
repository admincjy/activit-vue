package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Locale;

@Data
public class Base_UserLoginAttempt {

    @Display("登陆账号")
    @NotBlank
    @Length(max = 20)
    private String loginId;

    public String getLowerCaseLoginId(){
        return  this.loginId.toLowerCase(Locale.ENGLISH);
    }


    @Display("过期秒数")
    private Long expireSec;
}
