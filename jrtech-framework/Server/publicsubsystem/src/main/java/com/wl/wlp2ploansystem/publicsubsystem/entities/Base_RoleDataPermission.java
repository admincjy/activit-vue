package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_role_data_permission")
public class Base_RoleDataPermission  extends Entity {
    private String dataPermissionId;
    private String roleId;
}
