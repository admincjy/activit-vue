package com.wl.wlp2ploansystem.publicsubsystem.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Base_UserWithDepartmentNames extends Base_User{
    private String departmentNames;
}
