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

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Information;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_InformationService;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.Base_InformationQueryInDto;



@RestController
@RequestMapping(value = "/authapi/base_Information",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_InformationController {
    @Autowired
    private Base_InformationService service;

    /**
    * <p>
    * 根据 input 条件，分页读取信息公告
    * </p>
    *
    * @param input 分页,查询条件对象
    * @return 信息公告分页对象
    */
	@PostMapping("/getPagedList")
    @Log("分页读取信息公告列表") 
	@PreAuthorize("hasAuthority('menu_base_informationlist')")
    public PagedResultOutput<Base_Information> getPagedList(@Valid @RequestBody Base_InformationQueryInDto input) {
        EntityWrapper<Base_Information> ew = new EntityWrapper<>();
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
		 
						if(input.getTitle() != null){
			ew.like("title",input.getTitle());//标题
		}
								if(input.getSummary() != null){
			ew.like("summary",input.getSummary());//摘要
		}
								if(input.getContent() != null){
			ew.like("content",input.getContent());//内容
		}
									if(input.getClassificationIdList() != null && input.getClassificationIdList().size() > 0 ){
				ew.in("classificationId",input.getClassificationIdList());//分类
			}
									if(input.getTypeList() != null && input.getTypeList().size() > 0 ){
				ew.in("type",input.getTypeList());//类别：文章，下载
			}
									if(input.getStatusList() != null && input.getStatusList().size() > 0 ){
				ew.in("status",input.getStatusList());//状态
			}
								
			if(input.getStatusDateBegin() != null){
				ew.ge("statusDate",input.getStatusDateBegin());//状态日期
			}
			if(input.getStatusDateEnd() != null){
				ew.le("statusDate",input.getStatusDateEnd());//状态日期
			} 
								if(input.getStatusOperator() != null){
			ew.like("statusOperator",input.getStatusOperator());//状态操作人
		}
				
        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }

        Page<Base_Information> pager = new Page<Base_Information>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_Information> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

	 /**
    * <p>
    * 根据 input 条件，读取信息公告列表
    * </p>
    *
    * @param input 查询条件对象
    * @return 信息公告对象列表
    */
    @PostMapping("/getList")
    @Log("读取信息公告列表")
    @PreAuthorize("hasAuthority('menu_base_informationlist')")
    public List<Base_Information> getList(@Valid @RequestBody CommonCategoryIdAndSortingDto input) {
        EntityWrapper<Base_Information> ew = new EntityWrapper<>();
        //ew.eq("loanDocId",input.getCategoryId());


        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }


        List<Base_Information> results = service.getList(ew);

        return results;

    }
	
   	
	/**
    * <p>
    * 根据id，读取信息公告
    * </p>
    *
    * @param id 主键
    * @return 信息公告对象
    */
    @PostMapping("/get")
    @Log("读取信息公告对象")
    @PreAuthorize("hasAuthority('menu_base_informationlist')")
    public Base_Information get(String id){

        return  service.get(id);
    }
	
	/**
    * <p>
    * 新增/修改信息公告
    * </p>
    *
    * @param input 信息公告对象
    * @return 新的信息公告对象:
    */
    @PostMapping("/save")
    @Log("新增/修改信息公告")
    @PreAuthorize("hasAuthority('menu_base_informationlist')")
    public Base_Information save(@Valid @RequestBody Base_Information input) {
        String id =  service.save(input);

        return this.get(id);
    }
	
	/**
    * <p>
    * 删除信息公告
    * </p>
    *
    * @param id 主键 
    */
    @PostMapping("/delete")
    @Log("删除信息公告")
    @PreAuthorize("hasAuthority('menu_base_informationlist')")
    public void delete(String id) {
        service.delete(id);
    }
	/**
    * <p>
    * 批量删除信息公告
    * </p>
    *
    * @param ids 主键列表 
    */
    @PostMapping("/batchDelete")
    @Log("批量删除信息公告")
    @PreAuthorize("hasAuthority('menu_base_informationlist')")
    public Integer batchDelete(@RequestBody List<String> ids) {
        return service.batchDelete(ids);
    }
}