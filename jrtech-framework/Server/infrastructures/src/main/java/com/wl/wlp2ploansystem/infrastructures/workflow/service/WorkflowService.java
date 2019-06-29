package com.wl.wlp2ploansystem.infrastructures.workflow.service;

import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("workflowService")
@Transactional
public class WorkflowService {


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskPersistService taskPersistService;

    /**
     * 获取岗位的所属任务数量最少的用户Id(工作流流程定义时使用）
     *
     * @param execution 流程执行信息
     * @param roleId    岗位
     * @return 用户Id
     */
    public String getRoleUserOfExecution(DelegateExecution execution, String roleId) {

        String processDefinitionId = execution.getProcessDefinitionId();
        String processDefinitionKey = getProcessDefinition(processDefinitionId).getKey();
        String activityId = execution.getCurrentActivityId();

        return getRoleUser(processDefinitionKey, activityId, roleId);
    }

    /**
     * 获取岗位的所属用户Id列表(工作流流程定义时使用）
     *
     * @param execution 流程执行信息
     * @param roleId    岗位
     * @return 用户Id列表
     */
    public List<String> getRoleUsersOfExecution(DelegateExecution execution, String roleId) {

        String processDefinitionId = execution.getProcessDefinitionId();
        String processDefinitionKey = getProcessDefinition(processDefinitionId).getKey();
        String activityId = execution.getCurrentActivityId();

        return getRoleUsers(processDefinitionKey, activityId, roleId);
    }

    /**
     * 获取部门下特定岗位类别审批任务最少的用户Id
     *
     * @param execution      流程执行信息
     * @param roleCategoryId 岗位类别Id
     * @return 审批任务最少的用户Id
     */
    public String getDepartmentRoleCategoryUserOfExecution(DelegateExecution execution, String roleCategoryId) {

        String processDefinitionId = execution.getProcessDefinitionId();
        String processDefinitionKey = getProcessDefinition(processDefinitionId).getKey();
        String activityId = execution.getCurrentActivityId();
        WF_BusinessKey businessKey = WF_BusinessKey.of(execution.getProcessBusinessKey());

        return getDepartmentRoleCategoryUser(processDefinitionKey, activityId, businessKey.getDepartmentId(), roleCategoryId);
    }

    /**
     * 获取岗位的所属用户Id列表
     *
     * @param processDefinitionKey 流程定义key，可空
     * @param activityId           流程结点，可空
     * @param roleId               岗位
     * @return 用户Id列表
     */
    public List<String> getRoleUsers(String processDefinitionKey, String activityId, String roleId) {

        List<String> users = taskPersistService.getRoleUsers(processDefinitionKey, activityId,
                roleId, "all");
        return users;
    }

    /**
     * 获取岗位的所属任务数量最少的用户Id
     *
     * @param activityId 流程结点，可空
     * @param roleId     岗位
     * @return 用户Id列表
     */
    public String getRoleUser(String processDefinitionKey, String activityId, String roleId) {

        List<String> users = taskPersistService.getRoleUsers(processDefinitionKey, activityId,
                roleId, "avg");
        return users.stream().findFirst().orElse(null);
    }

    /**
     * 获取部门(departmentId)下岗位类别(roleCategoryId)审批任务最少的用户Id
     *
     * @param processDefinitionKey 流程定义key
     * @param activityId           流程结点
     * @param departmentId         部门Id
     * @param roleCategoryId       岗位类别Id
     * @return 审批任务最少的用户Id
     */
    public String getDepartmentRoleCategoryUser(String processDefinitionKey, String activityId, String departmentId, String roleCategoryId) {

        List<String> users = taskPersistService.getRoleCategoryUsers(processDefinitionKey, activityId, departmentId, roleCategoryId, "avg");
        return users.stream().findFirst().orElse(null);
    }

    /**
     * 启动流程
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务单据信息
     * @param originator           流程发起人Id
     * @param variables            工作流变量
     * @return 流程实例Id
     */
    @Transactional
    public String startWorkflow(String processDefinitionKey, WF_BusinessKey businessKey, String originator, Map<String, Object> variables) {
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(originator);

            processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey.toString(), variables);
            // String processInstanceId = processInstance.getId();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance.getId();
    }

    /***
     * 获取流程审批任务信息
     * @param sn 任务Id
     * @return 审批任务信息
     */
    public Task getTask(String sn) {

        return taskService.createTaskQuery().taskId(sn).singleResult();
    }

    /***
     * 获取流程审批任务变量信息
     * @param sn 任务Id
     * @return 审批任务变量信息
     */
    public Object getTaskVariable(String sn, String variableName) {

        return this.getTask(sn).getProcessVariables().get(variableName);
    }

    /***
     * 获取流程审批任务的上一个任务结点
     * @param sn 任务Id
     * @return 上一个任务结点
     */
    public String getBackActivityId(String sn) {

        // 取得当前任务.当前任务节点
        HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(sn)
                .singleResult();

        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask
                        .getProcessDefinitionId());


        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                .findActivity(currTask.getTaskDefinitionKey());
        // 取得上一步活动
        List<PvmTransition> incomingTransitions = currActivity
                .getIncomingTransitions();
        if(incomingTransitions.size()==1){
            TransitionImpl transitionImpl = (TransitionImpl) incomingTransitions.get(0);
            ActivityImpl activityImpl = transitionImpl.getSource();
            String type = (String) activityImpl.getProperty("type");
            if ("userTask".equals(type)) {// 用户任务
                return  transitionImpl.getId();
            } else if ("exclusiveGateway".equals(type)) { //// 并行路线
                ActivityImpl exclusiveGatewayActivityImpl = transitionImpl.getSource();
                if(exclusiveGatewayActivityImpl.getIncomingTransitions().size() == 1){
                    return  exclusiveGatewayActivityImpl.getIncomingTransitions().get(0).getId();
                }
            }
        } else {
            return null;
        }

        return null;
    }

    /***
     * 是否是委派任务
     * @param taskId 任务id
     * @return 是否是委派任务
     */
    public  boolean isDelegationTask(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        DelegationState delegationState = task.getDelegationState();
        return delegationState!=null && delegationState.equals(DelegationState.PENDING);
    }
    /**
     * 任务处理
     *
     * @param taskActionDto 任务对象
     */
    @Transactional
    public void handleTask(WF_TaskActionDto taskActionDto) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("action", taskActionDto.getAction());
        if (taskActionDto.getDataFields() != null) {
            variables.putAll(taskActionDto.getDataFields());
        }
        try {
            if (this.isDelegationTask(taskActionDto.getTaskId())){
                //如果是委托任务需要做处理
                taskService.resolveTask(taskActionDto.getTaskId());
            }
            else if (Boolean.TRUE.equals(taskActionDto.getClaim())) {
                taskService.claim(taskActionDto.getTaskId(), SecurityUtils.getCurrentUser().getId());
            } else {
                taskService.complete(taskActionDto.getTaskId(), variables);
            }
        } catch (ActivitiObjectNotFoundException ex) {
            throw new UserFriendlyException("任务已审批");
        }
    }

    /**
     * 任务跳转
     *
     * @param taskGotoDto 任务对象
     */
    public void goTo(WF_TaskGotoDto taskGotoDto) {
        jump(taskGotoDto.getTaskId(), taskGotoDto.getTargetTaskDefinitionKey(), taskGotoDto.getDataFields());
    }


    /***
     * 任务转办
     * @param taskId 任务id
     * @param assignee 新的审批人
     */
    public void setAssignee(String taskId, String assignee) {
        // 任务转办
        taskService.setAssignee(taskId, assignee);
    }

    /***
     * 任务委派
     * 任务分配给bill，然后bill把任务委派给henryyan，henryyan处理完成后任务回归到bill
     * @param taskId 任务id
     * @param userId 委派人
     */
    public void delegateTask(String taskId, String userId) {
        if (this.isDelegationTask(taskId)){
            throw  new UserFriendlyException("当前任务已是委派状态，无法再次委派");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(StringUtils.isEmpty(task.getAssignee())){
            throw  new UserFriendlyException("当前任务不存在处理人，无法委派");
        }
        taskService.delegateTask(taskId, userId);
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例Id
     * @param reason            删除原因
     */
    @Transactional
    public void deleteProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);//删除流程
    }

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例Id
     */
    @Transactional
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例Id
     */
    @Transactional
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /***
     * 获取任务的审批信息
     * @param sn 任务Id
     * @return 任务的审批信息对象
     */
    public HistoricTaskInstance getTaskHistoric(String sn) {

        HistoricTaskInstance taskHistory = historyService
                .createHistoricTaskInstanceQuery()
                .taskId(sn)
                .orderByTaskId()
                .desc()
                .singleResult();


        return taskHistory;
    }
    /**
     * 根据流程实例获取当前任务
     *
     * @param procInstId 流程实例Id
     */
    public Task getCurrentTaskByProcInstId(String procInstId) {
        Task currentTask =  taskService
                .createTaskQuery()
                .processInstanceId(procInstId).singleResult();

        return  currentTask;
    }

    /**
     * 查询流程定义对象
     *
     * @param processDefinitionId 流程定义ID
     * @return
     */
    private ProcessDefinition getProcessDefinition(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinition;
    }

    /**
     * @param taskId       当前任务Id
     * @param targetTaskDefinitionKey 目标任务节点（在模型定义里面的节点名称）
     */
    private void jump(final String taskId, String targetTaskDefinitionKey, Map<String, Object> variables) {

        Task currTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());

        // 当前节点  
        final ActivityImpl currActivity = pde.findActivity(currTask.getTaskDefinitionKey());
        //目标任务节点
        final ActivityImpl targetActivityImpl = pde.findActivity(targetTaskDefinitionKey);
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor()
                .execute(new Command<Void>() {
                    public Void execute(CommandContext commandContext) {
                        // 清空当前流向
                        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
                        // 创建新流向
                        TransitionImpl newTransition = currActivity.createOutgoingTransition();
                        // 设置新流向的目标节点
                        newTransition.setDestination(targetActivityImpl);
                        // 执行转向任务
                        taskService.complete(taskId, variables);
                        // 删除目标节点新流入
                        targetActivityImpl.getIncomingTransitions().remove(newTransition);
                        // 还原以前流向
                        restoreTransition(currActivity, oriPvmTransitionList);
                        return null;
                    }
                });
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        return oriPvmTransitionList;
    }

}
