package com.wl.wlp2ploansystem.infrastructures.workflow.servicetasks;

import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.infrastructures.workflow.service.TaskPersistService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NormalEndProcessInstST implements JavaDelegate {


    public  NormalEndProcessInstST(){
        SpringContextUtil.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }
    @Autowired
    private TaskPersistService taskPersistService;

    //流程变量
    private Expression status;

    //重写委托的提交方法
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String procInstId = execution.getProcessInstanceId();
        String statuss = (String) status.getValue(execution);
        taskPersistService.normalEndProcess(procInstId,statuss);
    }
}