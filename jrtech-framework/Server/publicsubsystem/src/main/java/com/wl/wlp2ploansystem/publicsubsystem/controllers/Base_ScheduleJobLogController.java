package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobLog;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_schedulejoblog", produces = { MediaType.APPLICATION_JSON_VALUE })
public class Base_ScheduleJobLogController {
    @Autowired
    private Base_ScheduleJobLogService service;

    /**
     * 定时任务日志列表
     */
    @PostMapping("/getList")
    @Log("分页读取定时任务日志信息")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public PagedResultOutput<Base_ScheduleJobLog> getList(@Valid @RequestBody CommonCategoryPagedInputDto input){
        EntityWrapper<Base_ScheduleJobLog> ew = new EntityWrapper<Base_ScheduleJobLog>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getCategoryId())) {

            ew.andNew().eq("jobId", input.getCategoryId());
        }
        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("jobName", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_ScheduleJobLog> pager = new Page<Base_ScheduleJobLog>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_ScheduleJobLog> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<Base_ScheduleJobLog>(results.getTotal(), results.getRecords());
    }

    /**
     * 定时任务日志信息
     */
    @PostMapping("/get")
    @Log("读取定时任务日志信息")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public Base_ScheduleJobLog get(String id){
        Base_ScheduleJobLog jobLog = service.get(id);

        return  jobLog;
    }
    /**
     * 批量删除定时任务日志信息
     */
    @Log("批量删除定时任务日志信息")
    @PostMapping("/batchDelete")
    @PreAuthorize("hasAuthority('menu_base_schedulejob')")
    public void batchDelete( @RequestBody List<String> ids){
        service.batchDelete(ids);

    }
}

