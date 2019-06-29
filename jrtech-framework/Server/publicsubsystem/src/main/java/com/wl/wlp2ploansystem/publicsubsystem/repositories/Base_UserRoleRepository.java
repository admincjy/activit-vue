package com.wl.wlp2ploansystem.publicsubsystem.repositories;
 
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserRole;

public interface Base_UserRoleRepository extends BaseRepository<Base_UserRole> {
	void saveUserRoles(@Param("userId") String userId,@Param("roleIds") List<String> roleIds);
}
