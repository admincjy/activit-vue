package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.SimpleTreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Role;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RoleWithPermissionIds;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;

import java.util.Collection;
import java.util.List;

public interface Base_RoleService extends BaseService {

	Base_Role get(String id);

	Integer selectCount(Wrapper<Base_Role> roleEntityWrapper);

	Collection<TreeObject<SimpleTreeObject>> getOrgRoleTree();

	List<Base_Role> getRolesOfOrg(CommonCategoryIdAndSortingDto input);

	List<Base_Role> getRoleListOfOrg(String organizationId);

	Base_RoleWithPermissionIds findOneWithPermissionIds(String id);

	List<Base_RoleWithPermissionIds> getWithPermissionIdsList(EntityWrapper<Base_RoleWithPermissionIds> entityWrapper);

	String save(Base_RoleWithPermissionIds input);

	void copyWhere(EntityWrapper<Base_RoleWithPermissionIds> roleWithPermissionIdsEntityWrapper,String newOrganizationId);

	void copy(String id);

	void delete(String id);

	void batchDelete(List<String> ids);

	void deleteByOrganizationId(String organizationId);

	Collection<Base_User> getRoleUserList(String roleId);
	Collection<Base_User> getRoleCategoryUsers(String departmentId,String roleCategoryId);

}