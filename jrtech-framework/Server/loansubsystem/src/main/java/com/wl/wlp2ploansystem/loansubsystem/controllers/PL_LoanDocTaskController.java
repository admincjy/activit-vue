package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.loansubsystem.controllers.dto.PL_LoanDocTaskQueryInDto;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocTask;
import com.wl.wlp2ploansystem.loansubsystem.servicesimpl.PL_LoanDocTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/authapi/pl_loandoctask",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_LoanDocTaskController {

    @Autowired
    private PL_LoanDocTaskServiceImpl service;

    @PostMapping("/getDraftedOrReRequestList")
    @Log("读取草稿或者驳回借款单据列表")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyInputList')")
    public PagedResultOutput<PL_LoanDocTask> getDraftedOrReRequestList(@RequestBody CommonPagedInputDto input){
        EntityWrapper<PL_LoanDocTask> ew = new EntityWrapper<>();


        if(!SecurityUtils.getCurrentUser().isSystemAdmin()) {
            ew.where("( (taskActId='enter' and (taskAssignee={0} or taskCandidate={1})) "
                            + " or  (processInstStatus='Draft' and (customerMRId={2} or gmtCreatedBy={3})) ) ",
                    SecurityUtils.getCurrentUser().getId(),
                    SecurityUtils.getCurrentUser().getId(),
                    SecurityUtils.getCurrentUser().getId()
                    , SecurityUtils.getCurrentUser().getId());
        }else{
            ew.where("( taskActId='enter'  or processInstStatus='Draft' ) ",
                    SecurityUtils.getCurrentUser().getId(),
                    SecurityUtils.getCurrentUser().getId()
            );
        }

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

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
            ew.orderBy("id", false);
        }

        Page<PL_LoanDocTask> pager = new Page<PL_LoanDocTask>(input.getSkipCount(), input.getMaxResultCount());


        Page<PL_LoanDocTask> results = service.getTaskPagedList(pager, ew);

        return new PagedResultOutput<PL_LoanDocTask>(results.getTotal(), results.getRecords());
    }

    @PostMapping("/getTaskList")
    @Log("读取待办借款单据列表")
    public PagedResultOutput<PL_LoanDocTask> getTaskList(@RequestBody PL_LoanDocTaskQueryInDto input){
        EntityWrapper<PL_LoanDocTask> ew = new EntityWrapper<>();

        ew.eq("processInstStatus","Approving");
        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if(!SecurityUtils.getCurrentUser().isSystemAdmin()){
            ew.andNew().eq("taskAssignee",SecurityUtils.getCurrentUser().getId())
                    .or()
                    .eq("taskCandidate",SecurityUtils.getCurrentUser().getId());
        }

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


        Page<PL_LoanDocTask> results = service.getTaskPagedList(pager, ew);

        return new PagedResultOutput<PL_LoanDocTask>(results.getTotal(), results.getRecords());
    }



}
