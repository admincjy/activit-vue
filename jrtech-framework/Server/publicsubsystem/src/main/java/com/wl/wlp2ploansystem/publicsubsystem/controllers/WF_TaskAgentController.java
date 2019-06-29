package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.*;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgent;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgentFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_TaskAgentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;



@RestController
@RequestMapping(value = "/authapi/wF_TaskAgents",produces = {MediaType.APPLICATION_JSON_VALUE})
public class WF_TaskAgentController {
    @Autowired
    private WF_TaskAgentService service;

    /**
    * <p>
    * 根据 input 条件，分页读取任务授权
    * </p>
    *
    * @param input 分页,查询条件对象
    * @return 任务授权分页对象
    */
	@PostMapping("/getMyPagedList")
    @Log("分页读取任务授权列表")
    public PagedResultOutput<WF_TaskAgentFull> getMyPagedList(@Valid @RequestBody CommonCategoryPagedInputDto input) {
        EntityWrapper<WF_TaskAgentFull> ew = new EntityWrapper<>();
		CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if(!SecurityUtils.getCurrentUser().isSystemAdmin()){
            ew.andNew().eq("assignee", SecurityUtils.getCurrentUser().getId());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }

        Page<WF_TaskAgentFull> pager = new Page<WF_TaskAgentFull>(input.getSkipCount(), input.getMaxResultCount());

        Page<WF_TaskAgentFull> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

	 /**
    * <p>
    * 根据 input 条件，读取任务授权列表
    * </p>
    *
    * @param input 查询条件对象
    * @return 任务授权对象列表
    */
    @PostMapping("/getList")
    @Log("读取任务授权列表")
    public List<WF_TaskAgent> getList(@Valid @RequestBody CommonCategoryIdAndSortingDto input) {
        EntityWrapper<WF_TaskAgent> ew = new EntityWrapper<>();
        //ew.eq("loanDocId",input.getCategoryId());


        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }


        List<WF_TaskAgent> results = service.getList(ew);

        return results;

    }
	
   	
	/**
    * <p>
    * 根据id，读取任务授权
    * </p>
    *
    * @param id 主键
    * @return 任务授权对象
    */
    @PostMapping("/get")
    @Log("读取任务授权对象")
   @PreAuthorize("hasAuthority('menu_wf_taskagentlist')")
    public WF_TaskAgentFull get(String id){

        return  service.get(id);
    }
	
	/**
    * <p>
    * 新增/修改任务授权
    * </p>
    *
    * @param input 任务授权对象
    * @return HashMap<String,String>存储id信息:
    */
    @PostMapping("/save")
    @Log("新增/修改任务授权")
    public WF_TaskAgent save(@Valid @RequestBody WF_TaskAgent input) {
        String id =  service.save(input);

        return this.get(id);
    }
    /**
     * <p>
     * 批量添加任务授权
     * </p>
     *
     * @param list 任务授权对象列表
     */
    @PostMapping("/batchSetAssignee")
    @Log("批量添加任务授权")
    public void batchSetAssignee(@Valid @RequestBody  List<WF_TaskAgent> list) {
        list.forEach(p->{
            p.setAssignee(SecurityUtils.getCurrentUser().getId());
            p.setStatus(WF_TaskAgent.WF_TaskAgent_Status_Enabled);
        });
        service.insertList(list);

    }
	/**
    * <p>
    * 删除任务授权
    * </p>
    *
    * @param id 主键 
    */
    @PostMapping("/delete")
    @Log("删除任务授权")
    public void delete(String id) {
        service.delete(id);
    }
	/**
    * <p>
    * 批量删除任务授权
    * </p>
    *
    * @param ids 主键列表 
    */
    @PostMapping("/batchDelete")
    @Log("批量删除任务授权")
    public Integer batchDelete(@RequestBody List<String> ids) {
         return service.batchDelete(ids);
    }

    /**
     * <p>
     * 批量启用任务授权
     * </p>
     *
     * @param ids 主键列表
     */
    @PostMapping("/batchEnable")
    @Log("批量启用任务授权")
    public Integer batchEnable(@RequestBody List<String> ids) {
        return service.batchEnabledOrDisable(ids,true);
    }

    /**
     * <p>
     * 批量禁用任务授权
     * </p>
     *
     * @param ids 主键列表
     */
    @PostMapping("/batchDisable")
    @Log("批量禁用任务授权")
    public Integer batchDisable(@RequestBody List<String> ids) {
        return service.batchEnabledOrDisable(ids,false);
    }
}