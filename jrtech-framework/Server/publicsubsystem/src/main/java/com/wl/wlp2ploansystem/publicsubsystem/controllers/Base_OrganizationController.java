package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_OrganizationService;
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
@RequestMapping(value = "/authapi/base_Organization", produces = { MediaType.APPLICATION_JSON_VALUE })
public class Base_OrganizationController {

	@Autowired
	private Base_OrganizationService service;
	
	@RequestMapping(value = "/getTreeOrganizations", method = RequestMethod.POST)
	@Log("读取组织机构列表（树形）")
	public Collection<TreeObject<Base_Organization>> getTreeOrganizations(){
		return service.getTreeOrganizations();
	}
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@Log("读取组织机构信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public Base_Organization get(String id){
		return service.get(id);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Log("新增/修改组织机构信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public HashMap<String,String> save(@RequestBody Base_Organization input){
		String id= service.save(input);

		HashMap<String,String> result = new HashMap<String,String>();
		result.put("id",id);

		return result;
	}
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	@Log("复制组织机构信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void copy(String id){
		service.copy(id);
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Log("删除组织机构信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void delete(String id){
		  service.delete(id);
	}
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@Log("批量删除组织机构信息")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void batchDelete(@RequestBody List<String> ids){
		 service.batchDelete(ids);
	}
	@RequestMapping(value = "/updateParentId", method = RequestMethod.POST)
	@Log("修改组织机构的上级")
	@PreAuthorize("hasAuthority('menu_base_organization')")
	public void updateParentId(String sourceId, String toParentId){
		 service.updateParentId(sourceId,toParentId);
	}
}
