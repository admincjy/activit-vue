package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_Navigation",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_NavigationController {

	@Autowired
	private Base_NavigationService service;
	

	@RequestMapping(value = "/getAll", method = RequestMethod.POST)
	@Log(disabled = true)
	@PreAuthorize("hasAuthority('menu_base_role')")
	public Collection<Base_Navigation> getAll(){
		
		return service.getAll();
	}
	@RequestMapping(value = "/getAuthoritiyAll", method = RequestMethod.POST)
	@Log(disabled = true)
	public Collection<Base_Navigation> getAuthoritiyAll(){
		
		return service.getAuthoritiyAll();
	}

	@PostMapping("/getTreeAll")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public Collection<TreeObject<Base_Navigation>> getTreeAll(){

		return service.getTreeAll();
	}

	/**
	 * <p>
	 * 根据id，读取菜单导航
	 * </p>
	 *
	 * @param id 主键
	 * @return 菜单导航对象
	 */
	@PostMapping("/get")
	@Log("读取菜单导航对象")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public Base_Navigation get(String id){

		return  service.get(id);
	}

	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	@PostMapping("/updateParentIdAndSortIndex")
	@Log("更新父节点及序号")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public void updateParentIdAndSortIndex(@Valid @RequestBody  TreeNodeDropInputDto inputDto){
		service.updateParentIdAndSortIndex(inputDto);
	}
	/**
	 * <p>
	 * 新增/修改菜单导航
	 * </p>
	 *
	 * @param input 菜单导航对象
	 * @return HashMap<String,String>存储id信息:
	 */
	@PostMapping("/save")
	@Log("新增/修改菜单导航")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public Base_Navigation save(@Valid @RequestBody Base_Navigation input) {
		String id =  service.save(input);

		return this.get(id);
	}

	/**
	 * <p>
	 * 删除菜单导航
	 * </p>
	 *
	 * @param id 主键
	 */
	@PostMapping("/delete")
	@Log("删除菜单导航")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public void delete(String id) {
		service.delete(id);
	}
	/**
	 * <p>
	 * 批量删除菜单导航
	 * </p>
	 *
	 * @param ids 主键列表
	 */
	@PostMapping("/batchDelete")
	@Log("批量删除菜单导航")
	@PreAuthorize("hasAuthority('menu_base_navigationlist')")
	public void batchDelete(@RequestBody List<String> ids) {
		service.batchDelete(ids);
	}
}
