package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Base_UserChangePasswordDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String OldPassword;

    @NotNull
    private String NewPassword;

}