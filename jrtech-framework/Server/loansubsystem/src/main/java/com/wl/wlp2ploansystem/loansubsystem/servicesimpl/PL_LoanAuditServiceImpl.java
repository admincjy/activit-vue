package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanAuditService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocTaskService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_AttachmentService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PL_LoanAuditServiceImpl extends BaseServiceImpl implements PL_LoanAuditService {

    @Autowired
    private PL_LoanDocService loanApplyDocService;

    @Autowired
    private PL_LoanDocTaskService taskService;

    @Autowired
    private Base_AttachmentService attachmentService;

    @Autowired
    private WF_WorkflowService workflowService;


    @Override
    @Transactional
    public void approve(WF_TaskActionDto taskActionDto){
        workflowService.handleTask(taskActionDto);
    }
    @Override
    @Transactional
    public void decline(WF_TaskGotoDto taskGotoDto){
        taskService.commonDecline(taskGotoDto);
    }
    @Override
    @Transactional
    public void gotoEnter(WF_TaskGotoDto taskGotoDto){
        taskService.commonGotoEnter(taskGotoDto);
    }

    @Override
    @Transactional
    public void claim(WF_TaskActionDto taskActionDto){
        taskActionDto.setClaim(true);
        workflowService.handleTask(taskActionDto);
    }

}