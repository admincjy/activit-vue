package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus;
import com.wl.wlp2ploansystem.infrastructures.workflow.service.WorkflowService;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_ProcessInstGotoDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInst;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_ProcessInstHistoryService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_ProcessInstService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_WorkflowStartInDto;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流服务
 */
@Service
public class WF_WorkflowServiceImpl extends BaseServiceImpl implements WF_WorkflowService {

    @Autowired
   private WorkflowService workflowService;
    @Autowired
    private WF_ProcessInstService processInstService;

    @Autowired
    private WF_ProcessInstHistoryService processInstHistoryService;

    @Autowired
    private Base_UserService userService;

    /**
     * 启动流程
     * @param inDto 流程启动输入参数
     * @return 流程实例Id
     */
   @Override
   @Transactional
    public String startWorkflow(WF_WorkflowStartInDto inDto) {

       String processDefinitionKey = inDto.getProcessDefinitionKey();
       String originator  = inDto.getOriginator();
       String departmentId  = inDto.getDepartmentId();
       String businessId  = inDto.getBusinessId();
       Map<String, Object> variables  = inDto.getVariables();
       LocalDateTime saveDate  = inDto.getSaveDate();
       String folio =  inDto.getFolio();
       String remark  = inDto.getRemark();
        WF_BusinessKey businessKey = new WF_BusinessKey(originator,departmentId,businessId,folio);

        String procInstId = workflowService.startWorkflow(processDefinitionKey,businessKey,originator,variables);

        processInstService.createProcessInst(procInstId,businessId,processDefinitionKey,originator,saveDate,folio,remark);

        return  procInstId;
    }
    /**
     * 任务处理
     * @param taskActionDto 任务对象
     */
    @Override
    @Transactional
    public void handleTask(WF_TaskActionDto taskActionDto) {
        workflowService.handleTask(taskActionDto);

        HistoricTaskInstance historicTaskInstance = workflowService.getTaskHistoric(taskActionDto.getTaskId());
        processInstService.createProcessInstHistory(taskActionDto.getTaskId(),
                historicTaskInstance.getProcessInstanceId(),
                historicTaskInstance.getTaskDefinitionKey(),
                historicTaskInstance.getName(),
                "asignee",
                SecurityUtils.getCurrentUser().getId(),
                taskActionDto.getAction(),
                taskActionDto.getResult(),
                taskActionDto.getTaskRemark(),
                LocalDateTime.now()
        );

    }
    /**
     * 任务跳转
     * @param taskGotoDto 任务对象
     */
    @Override
    @Transactional
    public  void goTo(WF_TaskGotoDto taskGotoDto){
        workflowService.goTo(taskGotoDto);

        HistoricTaskInstance historicTaskInstance = workflowService.getTaskHistoric(taskGotoDto.getTaskId());
        processInstService.createProcessInstHistory(taskGotoDto.getTaskId(),
                historicTaskInstance.getProcessInstanceId(),
                historicTaskInstance.getTaskDefinitionKey(),
                historicTaskInstance.getName(),
                "asignee",
                SecurityUtils.getCurrentUser().getId(),
                taskGotoDto.getAction(),
                taskGotoDto.getResult(),
                taskGotoDto.getTaskRemark(),
                LocalDateTime.now());

    }
    /**
     * 根据流程实例任务跳转
     * @param input 流程跳转对象
     */
    @Override
    @Transactional(readOnly = true)
    public  void goToByProcessInstId(WF_ProcessInstGotoDto input){
        Task  currTask = workflowService.getCurrentTaskByProcInstId(input.getProcessInstId());
        WF_TaskGotoDto taskGotoDto = new WF_TaskGotoDto();
        taskGotoDto.setAction(input.getAction());
        taskGotoDto.setDocId(input.getDocId());
        taskGotoDto.setTaskId(currTask.getId());
        taskGotoDto.setClaim(Boolean.FALSE);
        taskGotoDto.setTaskRemark(input.getTaskRemark());
        taskGotoDto.setDataFields(input.getDataFields());
        taskGotoDto.setTargetTaskDefinitionKey(input.getTargetTaskDefinitionKey());
        taskGotoDto.setTaskStatus("流程跳转");

        this.goTo(taskGotoDto);

    }
    /***
     * 任务转办
     * @param taskId 任务id
     * @param assignee 新的审批人
     */
    @Override
    @Transactional
    public  void setAssignee(String taskId,String assignee,String remark){

        String assingeeName = userService.getUserName(assignee);
        // 任务转办
        workflowService.setAssignee(taskId,assignee);

        Task  currTask = workflowService.getTask(taskId);
        processInstService.createProcessInstHistory(taskId,
                currTask.getProcessInstanceId(),
                currTask.getTaskDefinitionKey(),
                currTask.getName(),
                "redirect",
                SecurityUtils.getCurrentUser().getId(),
                "setAssignee",
                "任务转办[" + assingeeName +"]",
                remark,
                LocalDateTime.now());
    }

    /***
     * 任务委派
     * 任务分配给bill，然后bill把任务委派给henryyan，henryyan处理完成后任务回归到bill
     * @param taskId 任务id
     * @param userId 委派人
     */
    public void delegateTask(String taskId, String userId,String remark) {

        String userName = userService.getUserName(userId);

        workflowService.delegateTask(taskId, userId);

        Task  currTask = workflowService.getTask(taskId);
        processInstService.createProcessInstHistory(taskId,
                currTask.getProcessInstanceId(),
                currTask.getTaskDefinitionKey(),
                currTask.getName(),
                "delegate",
                SecurityUtils.getCurrentUser().getId(),
                "delegateTask",
                "任务委派["  + userName  +"]",
                remark,
                LocalDateTime.now());
    }


    /**
     * 获取当前任务中流程变量值
     * @param sn 任务Id
     * @param variableName 变量名称
     * @return  变量值
     */
    @Override
    @Transactional(readOnly = true)
    public  Object getTaskVariable(String sn,String variableName){

        return  workflowService.getTask(sn).getProcessVariables().get(variableName);
    }
    /**
     * 获取当前任务所在审批结点
     * @param sn 任务Id
     * @return 审批结点Id
     */
    @Override
    @Transactional(readOnly = true)
    public  String getTaskActivityId(String sn){

        return  workflowService.getTask(sn).getTaskDefinitionKey();
    }
    /**
     * 获取当前任务上一个审批结点
     * @param sn 任务Id
     * @return 审批结点Id
     */
    @Override
    @Transactional(readOnly = true)
    public String getBackActivityId(String sn){
        return workflowService.getBackActivityId(sn);
    }
    /**
     * 删除流程实例
     * @param processInstanceId 流程实例Id
     * @param reason 删除原因
     */
    @Override
    @Transactional
    public  void deleteProcessInstance(String processInstanceId,String reason){

        WF_ProcessInst processInst = processInstService.get(processInstanceId);

        processInstService.delete(processInstanceId);
        processInstHistoryService.deleteByProcessInstId(processInstanceId);

        if(EProcessInstStatus.ReRequest.name().equals(processInst.getStatus())
                || EProcessInstStatus.Approving.name().equals(processInst.getStatus())
                || EProcessInstStatus.Suspended.name().equals(processInst.getStatus())) {
            workflowService.deleteProcessInstance(processInstanceId, reason);
        }

    }

    /**
     * 激活流程实例
     * @param processInstanceId 流程实例Id
     */
    @Override
    @Transactional
    public  void activateProcessInstance(String processInstanceId){
        workflowService.activateProcessInstance(processInstanceId);

        WF_ProcessInst processInst = processInstService.get(processInstanceId);
        String originalStatus = StringUtils.isEmpty(processInst.getOriginalStatus())?EProcessInstStatus.Approving.name():processInst.getOriginalStatus();
        processInstService.setProcessInstStatus(processInstanceId, processInst.getOriginalStatus());
    }
    /**
     * 挂起流程实例
     * @param processInstanceId 流程实例Id
     */
    @Override
    @Transactional
    public  void suspendProcessInstance(String processInstanceId){
        workflowService.suspendProcessInstance(processInstanceId);
        processInstService.setProcessInstStatus(processInstanceId, EProcessInstStatus.Suspended.name());
    }


    /***
     * 设置流程实例状态
     * @param instId 流程实例Id
     * @param status 状态 {@link com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus}
     */
    @Override
    @Transactional
    public void setProcessInstStatus(String instId, String status){
        processInstService.setProcessInstStatus(instId,status);
    }
    /**
     * 获取岗位审批任务最少的用户Id
     * @param processDefinitionKey 流程定义key
     * @param activityId 流程结点
     * @param roleId 岗位
     * @return 审批任务最少的用户Id
     */
    @Override
    @Transactional(readOnly = true)
    public  String getRoleUser(String processDefinitionKey, String activityId,String roleId){

        return  workflowService.getRoleUser(processDefinitionKey,activityId,roleId);
    }
    /**
     * 获取部门(departmentId)下岗位类别(roleCategoryId)审批任务最少的用户Id
     * @param processDefinitionKey 流程定义key
     * @param activityId  流程结点
     * @param departmentId  部门Id
     * @param roleCategoryId 岗位类别Id
     * @return 审批任务最少的用户Id
     */
    @Override
    @Transactional(readOnly = true)
    public  String getRoleCategoryUsers(String processDefinitionKey, String activityId,String departmentId, String roleCategoryId){
        return  workflowService.getDepartmentRoleCategoryUser(processDefinitionKey,activityId,departmentId,roleCategoryId);
    }
}
