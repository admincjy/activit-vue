package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocTask;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocTaskService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_RptCommonQueryInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping(value = "/authapi/pl_report",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_ReportController {

    @Autowired
    private PL_LoanDocTaskService applyTaskService;

    @Autowired
    private PL_LoanDocService loanDocService;

    @PostMapping("/getLoanApplyProgessList")
    @Log("读取申请进度列表")
    @PreAuthorize("hasAuthority('menu_pl_rpt_loanapplyprocess')")
    public PagedResultOutput<PL_LoanDocTask> getLoanApplyProgessList(@RequestBody PL_RptCommonQueryInDto input){
        EntityWrapper<PL_LoanDocTask> ew = new EntityWrapper<>();
        ew.isNotNull("processInstStatus");
        ew.notIn("processInstStatus", Arrays.asList(EProcessInstStatus.Draft.name()));
        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        ew.ge(input.getStartDate() != null,"taskCreateDate",input.getStartDate());
        ew.le(input.getEndDate() != null,"taskCreateDate",input.getEndDate());
        ew.eq(!StringUtils.isEmpty(input.getDocOwnUserId()),"docOwnUserId",input.getDocOwnUserId());
        ew.eq(!StringUtils.isEmpty(input.getLoanProductSubTypeId()),"loanProductSubTypeId",input.getLoanProductSubTypeId());
        ew.like(!StringUtils.isEmpty(input.getOrganizationId()),"docOwnGroupIdPath","/"+input.getOrganizationId() +"/");



        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("customerName", input.getSearchKey())
                    .or().
                    like("customerCardNO", input.getSearchKey())
                    .or().
                    like("customerCode", input.getSearchKey());
        }


        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("taskId", false);
        }

        Page<PL_LoanDocTask> pager = new Page<PL_LoanDocTask>(input.getSkipCount(), input.getMaxResultCount());


        //非系统管理员，做数据权限过滤,SecurityUtils.getCurrentUser().getDataPermissionIds():当前用户拥有的数据权限列表
        Page<PL_LoanDocTask> results = applyTaskService.getLoanTaskProgress(pager
                ,SecurityUtils.getCurrentUser().isSystemAdmin()
                ,Collections.singletonList(SecurityUtils.getCurrentUser().getId())
                ,SecurityUtils.getCurrentUser().getDataPermissionIds()
                , ew);

        return new PagedResultOutput<PL_LoanDocTask>(results.getTotal(), results.getRecords());
    }



}
