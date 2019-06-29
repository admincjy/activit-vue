package com.wl.wlp2ploansystem.infrastructures.workflow.config;


import com.wl.wlp2ploansystem.infrastructures.workflow.eventListener.TaskCreatedListener;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = { "org.activiti.rest.diagram", "org.activiti.rest"}, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {
                RestController.class }) }, useDefaultFilters = false, lazyInit = false)
public class ActivitiConfig {

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DataSource druidDataSource;


    @Autowired
    private TaskCreatedListener taskCreatedListener;



    @Bean
    public SpringProcessEngineConfiguration getProcessEngineConfiguration(){
        SpringProcessEngineConfiguration config =
                new SpringProcessEngineConfiguration();
        config.setDataSource(druidDataSource);
        config.setTransactionManager(transactionManager);
        config.setDatabaseType("mysql");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setDbIdentityUsed(false);
        Map<String, List<ActivitiEventListener>> typedListeners = new HashMap<>();
        //https://segmentfault.com/a/1190000014194516
        typedListeners.put(ActivitiEventType.TASK_CREATED.name(), Collections.singletonList(taskCreatedListener));
        //typedListeners.put("TASK_ASSIGNED", Collections.singletonList(taskCreatedListener));
        config.setTypedEventListeners(typedListeners);
        return config;
    }

    @Primary
    @Bean
    public TaskExecutor primaryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        return executor;
    }

}