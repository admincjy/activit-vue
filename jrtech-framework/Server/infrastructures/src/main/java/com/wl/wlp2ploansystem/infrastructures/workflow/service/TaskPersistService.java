package com.wl.wlp2ploansystem.infrastructures.workflow.service;

import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_AgentUserInfo;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TaskPersistService {
    List<String> getRoleUsers(String processDefinationKey,String actId,String roleId,String getUserMode);
    List<String> getRoleCategoryUsers(String processDefinationKey, String actId, String departmentId,String roleCategoryId, String getUserMode);

    void afterCreateTask(String id, String procInstId, String actId, String actName, String actUserId, String
            formUrl , String status, WF_BusinessKey businessKey, Map<String,Object> variables);

    void createProcessInstHistory(String taskId, String processInstId, String actId, String actName, String taskUserSource, String taskUserId, String doAction, String taskResult, String taskRemark, LocalDateTime actionDate);

    void setProcessInstStatus(String instId, String status);
    void normalEndProcess(String instId,String status);

    /***
     *获取用户特定流程可用的授权用户，可能为空
     * @param processDefinitionKey 流程定义
     * @param assignee 被授权人
     */
    WF_AgentUserInfo getAssigneeEnabledAgents(String  processDefinitionKey, String assignee);

}
