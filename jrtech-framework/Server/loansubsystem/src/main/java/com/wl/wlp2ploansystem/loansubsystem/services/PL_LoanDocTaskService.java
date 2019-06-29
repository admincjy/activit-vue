package com.wl.wlp2ploansystem.loansubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocTask;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.util.Collection;
import java.util.List;

public interface PL_LoanDocTaskService extends BaseService {

    void cancel(WF_TaskActionDto input);
    void commonApprove(WF_TaskActionDto input);
    void commonDecline(WF_TaskGotoDto input);
    void commonGotoEnter(WF_TaskGotoDto taskGotoDto);
    void commonGoBack(WF_TaskActionDto input);

    Page<PL_LoanDocTask> getTaskPagedList(Page<PL_LoanDocTask> pager, EntityWrapper<PL_LoanDocTask> ew);
    Page<PL_LoanDocTask> getLoanTaskProgress(Page<PL_LoanDocTask> pager, boolean all, List<String> userIdList
            , Collection<String> dataPermissionIds, EntityWrapper<PL_LoanDocTask> ew);
}
