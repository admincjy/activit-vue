package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Role;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RoleWithPermissionIds;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface Base_RoleRepository extends BaseRepository<Base_Role> {
	List<Base_RoleWithPermissionIds> getWithPermissionIdsList(@Param("ew")Wrapper<Base_RoleWithPermissionIds> ew);
	//https://www.cnblogs.com/xuyatao/p/6962680.html
	List<Base_Role> getViewList(@Param("ew")Wrapper<Base_Role> ew);
}
