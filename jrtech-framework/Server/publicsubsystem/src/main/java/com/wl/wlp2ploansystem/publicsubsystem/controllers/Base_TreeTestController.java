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

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_TreeTest;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_TreeTestService;

import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject; 


@RestController
@RequestMapping(value = "/authapi/base_TreeTests",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_TreeTestController {
    @Autowired
    private Base_TreeTestService service;

    /**
    * <p>
    * 根据 input 条件，分页读取树形测试
    * </p>
    *
    * @param input 分页,查询条件对象
    * @return 树形测试分页对象
    */
	@PostMapping("/getPagedList")
    @Log("分页读取树形测试列表") 
	@PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public PagedResultOutput<Base_TreeTest> getPagedList(@Valid @RequestBody CommonCategoryPagedInputDto input) {
        EntityWrapper<Base_TreeTest> ew = new EntityWrapper<>();
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

        Page<Base_TreeTest> pager = new Page<Base_TreeTest>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_TreeTest> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

	 /**
    * <p>
    * 根据 input 条件，读取树形测试列表
    * </p>
    *
    * @param input 查询条件对象
    * @return 树形测试对象列表
    */
    @PostMapping("/getList")
    @Log("读取树形测试列表")
    @PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public List<Base_TreeTest> getList(@Valid @RequestBody CommonCategoryIdAndSortingDto input) {
        EntityWrapper<Base_TreeTest> ew = new EntityWrapper<>();
        //ew.eq("loanDocId",input.getCategoryId());


        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }


        List<Base_TreeTest> results = service.getList(ew);

        return results;

    }
	
   		 /**
     * <p>
     * 读取树形测试列表（树形）
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 树形测试树形结构结果
     */
	@PostMapping("/getTreeList")
	@Log("读取树形测试列表（树形）")
	@PreAuthorize("hasAuthority('menu_base_treetesttree')")
	public Collection<TreeObject<Base_TreeTest>> getTreeList(){
		return service.getTreeList(null);
	}
	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	@PostMapping("/updateParentIdAndSortIndex")
	@Log("更新父节点及序号")
	@PreAuthorize("hasAuthority('menu_base_treetesttree')")
	public void updateParentIdAndSortIndex(@Valid @RequestBody  TreeNodeDropInputDto inputDto){
		service.updateParentIdAndSortIndex(inputDto);
	}
	
	/**
    * <p>
    * 根据id，读取树形测试
    * </p>
    *
    * @param id 主键
    * @return 树形测试对象
    */
    @PostMapping("/get")
    @Log("读取树形测试对象")
    @PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public Base_TreeTest get(String id){

        return  service.get(id);
    }
	
	/**
    * <p>
    * 新增/修改树形测试
    * </p>
    *
    * @param input 树形测试对象
    * @return 新的树形测试对象:
    */
    @PostMapping("/save")
    @Log("新增/修改树形测试")
    @PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public Base_TreeTest save(@Valid @RequestBody Base_TreeTest input) {
        String id =  service.save(input);

        return this.get(id);
    }
	
	/**
    * <p>
    * 删除树形测试
    * </p>
    *
    * @param id 主键 
    */
    @PostMapping("/delete")
    @Log("删除树形测试")
    @PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public void delete(String id) {
        service.delete(id);
    }
	/**
    * <p>
    * 批量删除树形测试
    * </p>
    *
    * @param ids 主键列表 
    */
    @PostMapping("/batchDelete")
    @Log("批量删除树形测试")
    @PreAuthorize("hasAuthority('menu_base_treetesttree')")
    public Integer batchDelete(@RequestBody List<String> ids) {
        return service.batchDelete(ids);
    }
}