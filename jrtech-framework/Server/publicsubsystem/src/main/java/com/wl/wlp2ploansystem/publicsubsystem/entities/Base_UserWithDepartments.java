package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class Base_UserWithDepartments extends Base_User{

    @TableField(exist = false)
    private Collection<KeyStringValuePair> departmentlist;

}
