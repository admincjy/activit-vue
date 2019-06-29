package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.SimpleTreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Role;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RoleWithPermissionIds;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_RoleService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_role", produces = { MediaType.APPLICATION_JSON_VALUE })
public class Base_RoleController {

	@Autowired
	private Base_RoleService roleService;

	@Autowired
	private Base_UserService userService;

	@RequestMapping(value = "/getOrgRoleTree", method = RequestMethod.POST)
	@Log("读取组织机构所属岗位(树形)")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public Collection<TreeObject<SimpleTreeObject>> getOrgRoleTree() {
		return roleService.getOrgRoleTree();
	}
	@RequestMapping(value = "/getRolesOfOrg", method = RequestMethod.POST)
	@Log("读取组织机构所属岗位")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public List<Base_Role> getRolesOfOrg(@RequestBody CommonCategoryIdAndSortingDto input) {
		return roleService.getRolesOfOrg(input);
	}
	@RequestMapping(value = "/findOneWithPermissionIds", method = RequestMethod.POST)
	@Log("读取岗位及权限信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public Base_RoleWithPermissionIds findOneWithPermissionIds(String id) {
		return roleService.findOneWithPermissionIds(id);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Log("新增/修改岗位及权限信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public HashMap<String,String> save(@RequestBody  Base_RoleWithPermissionIds input) {
		String id =  roleService.save(input);

		HashMap<String,String> result = new HashMap<String,String>();
		result.put("id",id);

		return result;
	}
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	@Log("复制岗位及权限信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void copy(String id) {
		 roleService.copy(id);
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Log("删除岗位信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void delete(String id) {
		roleService.delete(id);
	}
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@Log("批量删除岗位信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void batchDelete(@RequestBody  List<String> ids) {
		roleService.batchDelete(ids);
	}
	@RequestMapping(value = "/getRoleUserList", method = RequestMethod.POST)
	@Log("读取岗位信息所属用户")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public Collection<Base_User> getRoleUserList(String roleId)
	{
		return roleService.getRoleUserList(roleId);
	}
}
