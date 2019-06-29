package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_ProcessInstGotoDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskGotoDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_DelegateInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_SetAssigneeInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_WorkflowStartInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/authapi/wf_workflow")
public class WF_WorkflowController {

    @Autowired
    private WF_WorkflowService workflowService;

    /**
     * 启动流程
     * @param inDto 流程启动输入参数
     * @return 流程实例Id
     */

    @PostMapping("/startWorkflow")
    @Display("启动流程")
    @Log("启动流程")
    public String startWorkflow(@Valid @RequestBody  WF_WorkflowStartInDto inDto) {

        return  workflowService.startWorkflow(inDto);
    }

    /**
     * 任务处理
     * @param taskActionDto 任务对象
     */

    @PostMapping("/handleTask")
    @Display("任务处理")
    @Log("任务处理")
    public void   handleTask(@Valid @RequestBody WF_TaskActionDto taskActionDto) {
         workflowService.handleTask(taskActionDto);
    }
    /**
     * 任务跳转
     * @param taskActionDto 任务对象
     */
    @PostMapping("/goTo")
    @Display("任务跳转")
    @Log("任务跳转")
    public void   goTo(@Valid @RequestBody WF_TaskGotoDto taskActionDto) {
        workflowService.goTo(taskActionDto);
    }
    /**
     * 任务跳转
     * @param taskActionDto 任务对象
     */
    @PostMapping("/goToByProcessInstId")
    @Display("根据流程实例任务跳转")
    @Log("根据流程实例任务跳转")
    public void   goToByProcessInstId(@Valid @RequestBody WF_ProcessInstGotoDto taskActionDto) {
        workflowService.goToByProcessInstId(taskActionDto);
    }

    /**
     * 激活流程实例
     * @param processInstanceId 流程实例Id
     */
    @PostMapping("/activateProcessInstance")
    @Display("激活流程实例")
    @Log("激活流程实例")
    public void   activateProcessInstance(String processInstanceId) {
        workflowService.activateProcessInstance(processInstanceId);
    }
    /**
     * 挂起流程实例
     * @param processInstanceId 流程实例Id
     */
    @PostMapping("/suspendProcessInstance")
    @Display("挂起流程实例")
    @Log("挂起流程实例")
    public void   suspendProcessInstance(String processInstanceId) {
        workflowService.suspendProcessInstance(processInstanceId);
    }
    /**
     * <p>
     * 任务转办
     * </p>
     *
     */
    @PostMapping("/setAssignee")
    @Log("任务转办")
    public void setAssignee(@Valid  @RequestBody WF_SetAssigneeInDto inDto) {
        workflowService.setAssignee(inDto.getTaskId(),inDto.getAssignee(),inDto.getRemark());
    }

    @PostMapping("/delegateTask")
    @Log("任务委派")
    public void delegateTask(@Valid  @RequestBody  WF_DelegateInDto inDto) {
        workflowService.delegateTask(inDto.getTaskId(),inDto.getUserId(),inDto.getRemark());
    }
}
