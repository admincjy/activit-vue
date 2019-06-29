package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class Base_UserAdminChangePasswordDto extends Base_UserChangePasswordDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String id;

    @NotNull
    private Boolean shouldChangePassword;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                '}';
    }
}
