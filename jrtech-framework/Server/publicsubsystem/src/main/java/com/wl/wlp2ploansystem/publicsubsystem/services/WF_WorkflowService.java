package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_ProcessInstGotoDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_WorkflowStartInDto;

public interface WF_WorkflowService extends BaseService {
    /**
     * 启动流程
     * @param inDto 流程启动输入参数
     * @return 流程实例Id
     */
    String startWorkflow(WF_WorkflowStartInDto inDto);
    /**
     * 任务处理
     * @param taskActionDto 任务对象
     */
    void handleTask(WF_TaskActionDto taskActionDto);
    /**
     * 任务跳转
     * @param taskGotoDto 任务对象
     */
    void goTo(WF_TaskGotoDto taskGotoDto);
    /**
     * 获取当前任务中流程变量值
     * @param sn 任务Id
     * @param variableName 变量名称
     * @return  变量值
     */
    Object getTaskVariable(String sn,String variableName);
    /**
     * 获取当前任务所在审批结点
     * @param sn 任务Id
     * @return 审批结点Id
     */
    String getTaskActivityId(String sn);
    /**
     * 获取当前任务上一个审批结点
     * @param sn 任务Id
     * @return 审批结点Id
     */
    String getBackActivityId(String sn);
    /**
     * 删除流程实例
     * @param processInstanceId 流程实例Id
     * @param reason 删除原因
     */
    void deleteProcessInstance(String processInstanceId,String reason);
    /**
     * 激活流程实例
     * @param processInstanceId 流程实例Id
     */
    void activateProcessInstance(String processInstanceId);
    /**
     * 挂起流程实例
     * @param processInstanceId 流程实例Id
     */
    void suspendProcessInstance(String processInstanceId);
    /***
     * 任务转办
     * @param taskId 任务id
     * @param assignee 新的审批人
     */
    void setAssignee(String taskId,String assignee,String remark);
    /***
     * 任务委派
     * 任务分配给bill，然后bill把任务委派给henryyan，henryyan处理完成后任务回归到bill
     * @param taskId 任务id
     * @param userId 委派人
     */
    void delegateTask(String taskId, String userId,String remark);
    /**
     * 根据流程实例任务跳转
     * @param input 流程跳转对象
     */
    void goToByProcessInstId(WF_ProcessInstGotoDto instGotoDto);
    /***
     * 设置流程实例状态
     * @param instId 流程实例Id
     * @param status 状态 {@link com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus}
     */
    void setProcessInstStatus(String instId, String status);
    /**
     * 获取岗位审批任务最少的用户Id
     * @param processDefinitionKey 流程定义key
     * @param activityId 流程结点
     * @param roleId 岗位
     * @return 审批任务最少的用户Id
     */
    String getRoleUser(String processDefinitionKey, String activityId,String roleId);
    /**
     * 获取部门(departmentId)下岗位类别(roleCategoryId)审批任务最少的用户Id
     * @param processDefinitionKey 流程定义key
     * @param activityId  流程结点
     * @param departmentId  部门Id
     * @param roleCategoryId 岗位类别Id
     * @return 审批任务最少的用户Id
     */
    String getRoleCategoryUsers(String processDefinationKey, String activityId, String departmentId, String roleCategoryId);

}
