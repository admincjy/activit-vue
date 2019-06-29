package com.wl.wlp2ploansystem.infrastructures.workflow.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.workflow.utils.ActivitiUtils;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.ProcessVO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProcessService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	public Page<ProcessVO> getProcessByPage(String category,Page<ProcessVO> page) {
	    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().latestVersion();
        if (StringUtils.isNotBlank(category)){
            query.processDefinitionCategory(category);
        }
		page.setTotal(((Long)query.count()).intValue());
		query.listPage(((int)page.getPages() - 1) * page.getSize(), page.getSize())
				.stream()
                .forEach(processDefinition -> {
					Deployment deployment = repositoryService.createDeploymentQuery()
							.deploymentId(processDefinition.getDeploymentId()).singleResult();
					page.getRecords().add(ActivitiUtils.toProcessVO(processDefinition, deployment));
		});
		page.setRecords((List<ProcessVO>) page.getRecords()
                .stream()
                .sorted(Comparator.comparing(ProcessVO::getDeploymentTime).reversed())
                .collect(Collectors.toList()));
		return page;
	}

	public InputStream resourceRead(String procDefId, String proInsId, String resType) throws Exception {

		if (StringUtils.isBlank(procDefId)){
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
			procDefId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();

		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}

		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		return resourceAsStream;
	}

	@Transactional(readOnly = false)
	public String updateStatus(String status, String procDefId) {
		if (status.equals("active")) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
		} else if (status.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
		}
		return status;
	}
}
