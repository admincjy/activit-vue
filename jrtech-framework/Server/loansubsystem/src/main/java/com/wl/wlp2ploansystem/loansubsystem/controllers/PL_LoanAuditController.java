package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authapi/pl_loanaudit",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_LoanAuditController {

    @Autowired
    private PL_LoanAuditService service;
    @PostMapping("/approve")
    @Log("审批通过")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyauditList')")
    public void approve(@RequestBody WF_TaskActionDto input){

        service.approve(input);
    }
    @PostMapping("/decline")
    @Log("拒绝")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyauditList')")
    public void decline(@RequestBody WF_TaskGotoDto input){
        service.decline(input);
    }
    @PostMapping("/goback")
    @Log("驳回")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyauditList')")
    public void gotoEnter(@RequestBody   WF_TaskGotoDto input){
        service.gotoEnter(input);
    }

    @PostMapping("/claim")
    @Log("签收")
    public void claim(@RequestBody WF_TaskActionDto input){
        service.claim(input);
    }
}
