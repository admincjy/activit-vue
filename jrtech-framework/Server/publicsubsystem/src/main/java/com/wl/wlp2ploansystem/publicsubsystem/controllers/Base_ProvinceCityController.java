package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.dto.*;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ProvinceCity;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ProvinceCityService;

import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject; 


@RestController
@RequestMapping(value = "/authapi/base_ProvinceCities",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_ProvinceCityController {
    @Autowired
    private Base_ProvinceCityService service;

    /**
    * <p>
    * 根据 input 条件，分页读取省市数据
    * </p>
    *
    * @param input 分页,查询条件对象
    * @return 省市数据分页对象
    */
	@PostMapping("/getPagedList")
    @Log("分页读取省市数据列表") 
	@PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public PagedResultOutput<Base_ProvinceCity> getPagedList(@Valid @RequestBody CommonCategoryPagedInputDto input) {
        EntityWrapper<Base_ProvinceCity> ew = new EntityWrapper<>();
		CommonSearchInputDto.resolveFilters(ew,input.getFilters());
        /*
        if(!SecurityUtils.getCurrentUser().isSystemAdmin()){
            ew.andNew().in("docOwnGroupId",SecurityUtils.getCurrentUser().getDataPermissionIds());
        }
        */ 

        if (!StringUtils.isEmpty(input.getSearchKey())) {
        /*
            ew.andNew().like("customerName", input.getSearchKey())
                    .or().
                    like("customerCardNO", input.getSearchKey())
                    .or().
                    like("customerCode", input.getSearchKey());
         */ 
         }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }

        Page<Base_ProvinceCity> pager = new Page<Base_ProvinceCity>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_ProvinceCity> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

	 /**
    * <p>
    * 根据 input 条件，读取省市数据列表
    * </p>
    *
    * @param input 查询条件对象
    * @return 省市数据对象列表
    */
    @PostMapping("/getList")
    @Log("读取省市数据列表")
    @PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public List<Base_ProvinceCity> getList(@Valid @RequestBody CommonCategoryIdAndSortingDto input) {
        EntityWrapper<Base_ProvinceCity> ew = new EntityWrapper<>();
        //ew.eq("loanDocId",input.getCategoryId());


        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }


        List<Base_ProvinceCity> results = service.getList(ew);

        return results;

    }
	
   		 /**
     * <p>
     * 读取省市数据列表（树形）
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 省市数据树形结构结果
     */
	@PostMapping("/getTreeList")
	@Log("读取省市数据列表（树形）")
	@PreAuthorize("hasAuthority('menu_base_provincecitytree')")
	public Collection<TreeObject<Base_ProvinceCity>> getTreeList(){
		return service.getTreeList(null);
	}
	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	@PostMapping("/updateParentIdAndSortIndex")
	@Log("更新父节点及序号")
	@PreAuthorize("hasAuthority('menu_base_provincecitytree')")
	public void updateParentIdAndSortIndex(@Valid @RequestBody  TreeNodeDropInputDto inputDto){
		service.updateParentIdAndSortIndex(inputDto);
	}
	
	/**
    * <p>
    * 根据id，读取省市数据
    * </p>
    *
    * @param id 主键
    * @return 省市数据对象
    */
    @PostMapping("/get")
    @Log("读取省市数据对象")
    @PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public Base_ProvinceCity get(String id){

        return  service.get(id);
    }
	
	/**
    * <p>
    * 新增/修改省市数据
    * </p>
    *
    * @param input 省市数据对象
    * @return 新的省市数据对象:
    */
    @PostMapping("/save")
    @Log("新增/修改省市数据")
    @PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public Base_ProvinceCity save(@Valid @RequestBody Base_ProvinceCity input) {
        String id =  service.save(input);

        return this.get(id);
    }
	
	/**
    * <p>
    * 删除省市数据
    * </p>
    *
    * @param id 主键 
    */
    @PostMapping("/delete")
    @Log("删除省市数据")
    @PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public void delete(String id) {
        service.delete(id);
    }
	/**
    * <p>
    * 批量删除省市数据
    * </p>
    *
    * @param ids 主键列表 
    */
    @PostMapping("/batchDelete")
    @Log("批量删除省市数据")
    @PreAuthorize("hasAuthority('menu_base_provincecitytree')")
    public Integer batchDelete(@RequestBody List<String> ids) {
        return service.batchDelete(ids);
    }
}