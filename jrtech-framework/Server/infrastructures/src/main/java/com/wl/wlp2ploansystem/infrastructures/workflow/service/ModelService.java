package com.wl.wlp2ploansystem.infrastructures.workflow.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service
public class ModelService {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Transactional(readOnly = false)
    public Model save(String id,String name, String key, String desc, String category) throws Exception {

            Model model = null;
            if(StringUtils.isEmpty(id)) {
                model = repositoryService.newModel();
            }else{
                model = this.get(id);
            }
            desc = StringUtils.defaultString(desc);
            model.setKey(StringUtils.defaultString(key));
            model.setName(name);
            model.setCategory(category);
            model.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(model.getKey()).count()+1)));

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, desc);
            model.setMetaInfo(modelObjectNode.toString());

            repositoryService.saveModel(model);

        if(StringUtils.isEmpty(id)) {
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode properties = objectMapper.createObjectNode();
            properties.put("process_id", key);
            properties.put("process_author", "lms");
            properties.put("name", name);
            properties.put("documentation", desc);
            editorNode.set("properties", properties);
            ObjectNode stencilset = objectMapper.createObjectNode();
            stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilset);
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));
        }else{
        }


            return model;

    }

    //activiti 流程模型复制
    @Transactional(readOnly = false)
    public Model copy(String id) throws Exception {

        Model sourceModel = this.get(id);
        ObjectNode sourceObjectNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(id));

        Model model = repositoryService.newModel();
        model.setKey(sourceModel.getKey() + "_" + IdGenerator.get());
        model.setName(sourceModel.getName()+"_copy");
        model.setCategory(sourceModel.getCategory());
        model.setVersion(1);
        repositoryService.saveModel(model);

        ObjectNode editorNode = sourceObjectNode.deepCopy();
        ObjectNode properties = objectMapper.createObjectNode();
        properties.put("process_id", model.getKey());
        properties.put("process_author", "lms");
        properties.put("name", model.getName());
        editorNode.set("properties", properties);

        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));

        return model;

    }

    public Page<Model> getMadelByPage(Page<Model> dt,String name) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();

        if (StringUtils.isNotBlank(name)) {
            modelQuery.modelNameLike("%" + name + "%");
        }
        dt.setTotal(((Long)modelQuery.count()).intValue());
        dt.setRecords(modelQuery.listPage(((int)dt.getPages() - 1) * dt.getSize(), dt.getSize()));
        return dt;
    }
    public List<Model> getMadelList() {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
        return  modelQuery.list();
    }
    public void delete(List<String> ids) {
        ids.stream().forEach(id -> repositoryService.deleteModel(id));
    }

    @Transactional(readOnly = false)
    public Model get(String id)   {

        // 获取模型
        Model model = repositoryService.getModel(id);

        return  model;
    }
    @Transactional(readOnly = false)
    public String deploy(String id) throws Exception {
        String message = "";

            // 获取模型
            Model model = repositoryService.getModel(id);
            ObjectNode objectNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(objectNode);

            String processName = model.getName();
            if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
                processName += ".bpmn20.xml";
            }

        try {
            // 部署流程
            Deployment deployment = repositoryService.createDeployment().name(model.getName())
                    .addBpmnModel(processName, bpmnModel)
                    .deploy();

            // 设置流程分类
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            list.stream().forEach(processDefinition ->
                    repositoryService.setProcessDefinitionCategory(processDefinition.getId(), model.getCategory()));
        }
        catch (Exception ex){
            throw  new UserFriendlyException("部署失败：" + ex.getMessage(),ex);
        }
        return message;
    }
	@Transactional(readOnly = false)
    public void deployFromFile(String id,File deployFile) throws Exception {
		 // 获取模型
		Model model = repositoryService.getModel(id);

		String processName = model.getName();
		if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
			processName += ".bpmn20.xml";
		}
		String fileName = deployFile.getName();
		if(!fileName.endsWith(".xml") && !fileName.endsWith(".bpmn") && !fileName.endsWith(".zip") && !fileName.endsWith(".bar")){
            throw new UserFriendlyException("无效的文件类型！");
        }
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(FileUtils.readFileToString(deployFile, Charset.forName("UTF-8")).trim().getBytes("UTF-8"));
            DeploymentBuilder deployBuilder = repositoryService.createDeployment().name(model.getName());
            if (fileName.endsWith(".xml") || fileName.endsWith(".bpmn")) {
                deployBuilder.addInputStream(processName, byteArrayInputStream).deploy();
            } else if (fileName.endsWith(".zip") || fileName.endsWith(".bar")) {
                deployBuilder.addZipInputStream(new ZipInputStream(byteArrayInputStream)).deploy();
            }
        }
        catch (Exception ex){
		    throw  new UserFriendlyException("文件部署失败：" + ex.getMessage(),ex);
        }
    }
    @Transactional(readOnly = false)
    public InputStream xmlRead(String id, String resourceType) throws Exception {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(id).latestVersion().singleResult();
        if(processDefinition == null){
            throw  new UserFriendlyException("流程尚未部署，无法查看代码");
        }
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                resourceName);
        return resourceAsStream;
    }

    @Transactional(readOnly = true)
    public Collection<FlowElement> getProcessActivities(String key)  throws Exception{
        // 获取模型
        Model model = repositoryService.createModelQuery().modelKey(key).singleResult();
        ObjectNode objectNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(objectNode);
        return bpmnModel.getMainProcess().getFlowElements();
    }

}
