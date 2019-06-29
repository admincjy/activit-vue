package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Log;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authapi/base_log")
@Display("操作日志")
public class Base_LogController {

    @Autowired
    private Base_LogService service;


    @PostMapping("/getList")
    @Display("分页获取操作日志")
    @Log(disabled = true)
    @PreAuthorize("hasAuthority('menu_base_log')")
    public PagedResultOutput<Base_Log> getList(@Valid @RequestBody CommonPagedInputDto input) {
        EntityWrapper<Base_Log> ew = new EntityWrapper<Base_Log>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("operation", input.getSearchKey())
                    .or().
                    like("userName", input.getSearchKey())
                    .or().
                    like("method", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_Log> pager = new Page<Base_Log>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_Log> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<Base_Log>(results.getTotal(), results.getRecords());

    }

    @PostMapping("/get")
    @Log(disabled = true)
    @PreAuthorize("hasAuthority('menu_base_log')")
    public Base_Log get(String id) {
        Base_Log result =  service.get(id);

        return result;
    }

    @PostMapping("/delete")
    @Log("删除系统日志")
    @PreAuthorize("hasAuthority('menu_base_log')")
    public void delete(String id) {
        service.delete(id);
    }

    @PostMapping("/batchDelete")
    @Log("批量删除系统日志")
    @PreAuthorize("hasAuthority('menu_base_log')")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}

