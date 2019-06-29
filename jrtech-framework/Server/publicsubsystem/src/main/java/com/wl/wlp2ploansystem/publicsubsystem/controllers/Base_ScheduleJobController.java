package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJob;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_schedulejob", produces = { MediaType.APPLICATION_JSON_VALUE })
public class Base_ScheduleJobController {
    @Autowired
    private Base_ScheduleJobService service;

    /**
     * 定时任务列表
     */
    @PostMapping("/getList")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public PagedResultOutput<Base_ScheduleJobFull> getList(@Valid @RequestBody CommonPagedInputDto input){
        EntityWrapper<Base_ScheduleJobFull> ew = new EntityWrapper<Base_ScheduleJobFull>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("name", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_ScheduleJobFull> pager = new Page<Base_ScheduleJobFull>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_ScheduleJobFull> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<Base_ScheduleJobFull>(results.getTotal(), results.getRecords());
    }

    /**
     * 定时任务信息
     */
    @Log("读取定时任务信息")
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public Base_ScheduleJob get(String id){
        Base_ScheduleJob schedule = service.get(id);

        return  schedule;
    }

    /**
     * 新增/修改定时任务
     */
    @Log("新增/修改定时任务")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public HashMap<String,String> save(@RequestBody Base_ScheduleJob input){

        String id =  service.save(input);

        HashMap<String,String> result = new HashMap<String,String>();
        result.put("id",id);

        return result;
    }

    /**
     * 批量删除定时任务
     */
    @Log("批量删除定时任务")
    @PostMapping("/batchDelete")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public void batchDelete( @RequestBody List<String> ids){
        String[] idStringList = new String[ids.size()];
        ids.toArray(idStringList);
         service.batchDelete(idStringList);

    }

    /**
     * 立即执行任务
     */
    @Log("批量立即执行任务")
    @PostMapping("/run")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public void run( @RequestBody List<String> ids){
        String[] idStringList = new String[ids.size()];
        ids.toArray(idStringList);
        service.run(idStringList);
    }

    /**
     * 暂停定时任务
     */
    @Log("批量暂停定时任务")
    @PostMapping("/pause")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public void pause( @RequestBody List<String> ids){
        String[] idStringList = new String[ids.size()];
        ids.toArray(idStringList);
        service.pause(idStringList);
    }

    /**
     * 恢复定时任务
     */
    @Log("批量恢复定时任务")
    @PostMapping("/resume")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public void resume( @RequestBody List<String> ids){
        String[] idStringList = new String[ids.size()];
        ids.toArray(idStringList);
        service.resume(idStringList);
    }
}
