package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RoleDataPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Base_RoleDataPermissionRepository  extends BaseRepository<Base_RoleDataPermission> {
    void saveRoleDataPermissions(@Param("roleId") String roleId, @Param("dataPermissionIds") List<String> dataPermissionIds);
}
