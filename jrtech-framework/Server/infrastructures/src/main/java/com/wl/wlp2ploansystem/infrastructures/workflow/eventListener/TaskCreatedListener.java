package com.wl.wlp2ploansystem.infrastructures.workflow.eventListener;

import com.wl.wlp2ploansystem.infrastructures.workflow.service.TaskPersistService;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_AgentUserInfo;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskCreatedListener implements ActivitiEventListener {

    @Autowired
    private TaskPersistService service;

    @Override
    public void onEvent(ActivitiEvent event){

        TaskEntity taskEntity = (TaskEntity)((ActivitiEntityEvent)event).getEntity();

        String targetTo = taskEntity.getAssignee();
        String status = "待办理";
        if(StringUtils.isEmpty(taskEntity.getAssignee())) {
            Set<IdentityLink> candidates = taskEntity.getCandidates();
            if(candidates.size() == 1){
                IdentityLink identityLink = candidates.stream().findFirst().orElse(null);
                if(!StringUtils.isEmpty(identityLink.getUserId())){ //候选人只有一个，自动签收
                    taskEntity.setAssignee(identityLink.getUserId());
                    targetTo = taskEntity.getAssignee();
                }
            }
        }
        if(StringUtils.isEmpty(taskEntity.getAssignee())){
            status="待签收";
            Set<String> targetToSet = taskEntity.getCandidates().stream().map(p->p.getUserId()).collect(Collectors.toSet());
            targetTo = String.join(",", targetToSet);
        }


        String processDefinitionKey = taskEntity.getProcessInstance().getProcessDefinitionKey();
        if(!StringUtils.isEmpty(taskEntity.getAssignee())){

            String originalAssignee = taskEntity.getAssignee();
            /***
             * 授权人
             */
            WF_AgentUserInfo attorney = service.getAssigneeEnabledAgents(processDefinitionKey,originalAssignee);
            if(attorney !=null){
                taskEntity.setAssignee(attorney.getId());
                service.createProcessInstHistory(taskEntity.getId(),
                        taskEntity.getProcessInstanceId(),
                        taskEntity.getTaskDefinitionKey(),
                        taskEntity.getName(),
                        "agent",
                        originalAssignee,
                        "agent",
                        "任务授权["+attorney.getName()+"]",
                        "",
                        LocalDateTime.now());
            }

            targetTo = taskEntity.getAssignee();
        }

        Map<String,Object> variables =  taskEntity.getProcessInstance().getVariables();
        WF_BusinessKey businessKey = WF_BusinessKey.of(taskEntity.getProcessInstance().getBusinessKey());

        service.afterCreateTask(
                taskEntity.getId(),
                taskEntity.getProcessInstanceId(),
                taskEntity.getTaskDefinitionKey(),
                taskEntity.getName(),
                targetTo,
                taskEntity.getFormKey(),
                status,
                businessKey,
                variables
        );



    }
    @Override
    public boolean isFailOnException(){
        return true;
    }
}