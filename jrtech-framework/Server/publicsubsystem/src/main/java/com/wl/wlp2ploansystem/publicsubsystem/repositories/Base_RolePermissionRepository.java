package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository; 
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RolePermission;

public interface Base_RolePermissionRepository extends BaseRepository<Base_RolePermission> {
	void saveRolePermissions(@Param("roleId") String roleId,@Param("permissionIds") List<String> permissionIds);
}
 
