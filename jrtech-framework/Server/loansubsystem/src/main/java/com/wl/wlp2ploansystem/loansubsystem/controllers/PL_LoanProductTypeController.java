package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductType;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanProductTypeService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanProductTypeFullDto;
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
@RequestMapping(value = "/authapi/pl_loanproducttype",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_LoanProductTypeController {
    @Autowired
    private PL_LoanProductTypeService service;

    @PostMapping("/getList")
    @PreAuthorize("hasAuthority('menu_pl_loanproducttype')")
    public PagedResultOutput<PL_LoanProductType> getList(@Valid @RequestBody CommonPagedInputDto input) {
        EntityWrapper<PL_LoanProductType> ew = new EntityWrapper<PL_LoanProductType>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        ew.like(!StringUtils.isEmpty(input.getSearchKey()), "name",
                input.getSearchKey()) ;

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("gmtCreatedOn", false);
        }

        Page<PL_LoanProductType> pager = new Page<PL_LoanProductType>(input.getSkipCount(), input.getMaxResultCount());

        Page<PL_LoanProductType> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<PL_LoanProductType>(results.getTotal(), results.getRecords());

    }
    @PostMapping("/get")
    @Log("读取借款产品信息")

    public PL_LoanProductTypeFullDto get(String id) {
        return service.get(id);
    }
    @PostMapping("/save")
    @Log("新增/借款产品信息")
    @PreAuthorize("hasAuthority('menu_pl_loanproducttype')")
    public PL_LoanProductTypeFullDto save(@RequestBody PL_LoanProductTypeFullDto input) {
        String id =  service.save(input);

        return this.get(id);
    }
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('menu_pl_loanproducttype')")
    public void delete(String id) {
        service.delete(id);
    }
    @PostMapping("/batchDelete")
    @PreAuthorize("hasAuthority('menu_pl_loanproducttype')")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}
