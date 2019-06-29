package com.wl.wlp2ploansystem.loansubsystem.services;

import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

public interface PL_LoanAuditService extends BaseService {
    void claim(WF_TaskActionDto taskActionDto);
    void approve(WF_TaskActionDto taskActionDto);
    void decline(WF_TaskGotoDto taskActionDto);
    void gotoEnter(WF_TaskGotoDto taskActionDto);
}
