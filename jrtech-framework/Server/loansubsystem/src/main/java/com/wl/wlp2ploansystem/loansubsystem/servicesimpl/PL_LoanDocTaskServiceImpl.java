package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDoc;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocTask;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanDocTaskRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocTaskService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class PL_LoanDocTaskServiceImpl extends BaseServiceImpl implements PL_LoanDocTaskService {

    @Autowired
    private PL_LoanDocService docService;

    @Autowired
    private PL_LoanDocTaskRepository repository;

    @Autowired
    private WF_WorkflowService workflowService;


    @Transactional
    public void commonDecline(WF_TaskGotoDto taskGotoDto)
    {
        String sn = taskGotoDto.getTaskId();

        if (StringUtils.isEmpty(sn)){
            throw new UserFriendlyException("单据状态错误，无法进行后续操作！");
        }

        taskGotoDto.setTargetTaskDefinitionKey("declinesetstatus");
        taskGotoDto.setTaskStatus("拒绝");
        workflowService.goTo(taskGotoDto);

        PL_LoanDoc docEntity = docService.get(taskGotoDto.getDocId());
        docEntity.setDocStatus(PL_LoanDoc.DocStatus_Declined);
        docEntity.setDocStatusDate(LocalDateTime.now());
        docService.save(docEntity);

    }
    @Override
    @Transactional
    public void commonGotoEnter(WF_TaskGotoDto taskGotoDto){
        taskGotoDto.setTargetTaskDefinitionKey("requestsetstatus");
        taskGotoDto.setTaskStatus("驳回");
        workflowService.goTo(taskGotoDto);
    }
    @Transactional
    public void commonApprove(WF_TaskActionDto input)
    {
        String sn = input.getTaskId();

        if (StringUtils.isEmpty(sn)){
            throw new UserFriendlyException("单据状态错误，无法进行后续操作！");
        }

        workflowService.handleTask(input);

    }
    @Transactional
    public void cancel(WF_TaskActionDto input)
    {
        String sn = input.getTaskId();

        if (StringUtils.isEmpty(sn)){
            throw new UserFriendlyException("单据状态错误，无法进行后续操作！");
        }

        workflowService.handleTask(input);

    }
    @Transactional
    public void commonGoBack(WF_TaskActionDto input)
    {
        String sn = input.getTaskId();

        if (StringUtils.isEmpty(sn)){
            throw new UserFriendlyException("单据状态错误，无法进行后续操作！");
        }

        workflowService.handleTask(input);
    }

    @Transactional(readOnly = true)
    public Page<PL_LoanDocTask> getTaskPagedList(Page<PL_LoanDocTask> pager, EntityWrapper<PL_LoanDocTask> ew){
        if (null != ew) {
            ew.orderBy(pager.getOrderByField(), pager.isAsc());
        }
        return pager.setRecords(repository.selectPage(pager, ew));
    }
    @Transactional(readOnly = true)
    public Page<PL_LoanDocTask> getLoanTaskProgress(Page<PL_LoanDocTask> pager, boolean all, List<String> userIdList
            , Collection<String> dataPermissionIds, EntityWrapper<PL_LoanDocTask> ew){
        if (null != ew) {
            ew.orderBy(pager.getOrderByField(), pager.isAsc());
        }
        return pager.setRecords(repository.getLoanTaskProgress(pager,all,userIdList,dataPermissionIds,ew));
    }


}
