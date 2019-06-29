package com.wl.wlp2ploansystem.publicsubsystem.repositories;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface Base_UserRepository extends BaseRepository<Base_User> {
	Base_User findOneByLoginId(String loginId);
	List<Base_UserWithAuthorities> getUserWithAuthoritiesList(@Param("ew")Wrapper<Base_UserWithAuthorities> ew);
	Base_UserWithRoleIds findOneWithRoleIds(String id);
	Collection<Base_User> getRoleUserList(String roleId);
	Collection<Base_User> getRolesUserList(@Param("roleIds") Collection<String> roleIds);
	List<Base_UserWithDepartmentNames> getUserWithDepartmentNamesList(Page<Base_UserWithDepartmentNames> page,@Param("ew")Wrapper<Base_UserWithDepartmentNames> ew);

	Base_UserWithDepartments getUserWithDepartments(@Param("id") String id);
	Collection<Base_User> getRoleCategoryUsers(@Param("departmentId")String departmentId,@Param("roleCategoryId")String roleCategoryId);

	List<Base_User> getPagedRoleCategoryUsers(Page<Base_User> page, @Param("departmentId")String departmentId,@Param("roleCategoryId")String roleCategoryId,@Param("ew")Wrapper<Base_User> ew);
}
