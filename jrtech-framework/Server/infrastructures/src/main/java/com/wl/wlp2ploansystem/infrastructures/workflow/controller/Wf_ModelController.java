package com.wl.wlp2ploansystem.infrastructures.workflow.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileParam;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileUploadUtils;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.CommonHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.FileHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.HttpHelper;
import com.wl.wlp2ploansystem.infrastructures.workflow.service.ModelService;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.ModelForm;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@Controller

/***
 * 工作流模型操作
 */
public class Wf_ModelController {

    @Autowired
    ModelService modelService;

    @Autowired
    ApplicationProperties applicationProperties;

    @RequestMapping(value = "/authapi/wf_Model/get", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("读取流程定义信息")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public ModelForm get(String id) throws  Exception {
        Model model = modelService.get(id);

        return new ModelForm(model);
    }
    @RequestMapping(value = "/authapi/wf_Model/save", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("新增/修改流程定义信息")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public ModelForm save(@RequestBody ModelForm form) throws  Exception {
       Model m =  modelService.save(form.getId(),form.getName(), form.getKey(), form.getDesc(), form.getCategory());

       return  new ModelForm(m);

    }
    @RequestMapping(value = "/authapi/wf_Model/copy", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("复制流程定义信息")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public void  copy(String id) throws  Exception {
        modelService.copy(id);
    }

    @RequestMapping(value = "/authapi/wf_Model/list", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("读取流程定义列表")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public PagedResultOutput<Model> list(@RequestBody CommonPagedInputDto input) {
        if (null == input) {
            return null;
        }
        Page<Model> pager = new Page<Model>(input.getSkipCount(), input.getMaxResultCount());

        Page<Model> results = modelService.getMadelByPage(pager,input.getSearchKey());
        return  new PagedResultOutput<>(results.getTotal(), results.getRecords());
    }
    @RequestMapping(value = "/authapi/wf_Model/delete", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("批量删除流程定义")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public void delete(@RequestBody List<String> ids) {

        modelService.delete(ids);

    }

    @RequestMapping(value = "/authapi/wf_Model/deploy", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @Log("部署流程定义")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public void deploy(String id) throws  Exception {
        modelService.deploy(id);

    }

    @Log("文件方式部署流程定义")
    @ResponseBody
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    @PostMapping("/authapi/wf_Model/deployFromFile")
    public void deployFromFile(HttpServletRequest request) throws  Exception {
        String id = request.getParameter("id");
        MultipartFileParam param = MultipartFileUploadUtils.parse(request);
        if (!param.isMultipart()) {
            return;
        }
        String globalDirPath = FileHelper.combarePath(applicationProperties.getFileupload().getFilePath(),"wf");
        String fileExten = param.getFileExtension();
        String fid = param.getFid();
        String fileName = param.getFileName();
        HttpHelper.multipartFileUpload(request,param,null,globalDirPath, p->{
            File newFile = new File(p);
            try {
                modelService.deployFromFile(id,newFile);
            } catch (Exception e) {
               throw new UserFriendlyException(e.getMessage(),e);
            }
        });
    }
    @Log("下载流程部署文件")
    @PostMapping(value = "/authapi/wf_Model/downDeployFile")
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public void downloadDeployFile(HttpServletRequest request,HttpServletResponse response) throws  Exception {
        String id = request.getParameter("id");
        Model model = modelService.get(id);

        String filename = model.getName();
        InputStream resourceAsStream = modelService.xmlRead(model.getKey(),"xml");
        InputStreamReader inputStreamReader= new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8.name());
        byte[] bytes =  IOUtils.toByteArray(inputStreamReader);
        HttpHelper.download(request,response,filename,bytes);

    }
    @Log("查看流程定义代码")
    @RequestMapping(value = "/authapi/wf_Model/codeRead", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @PreAuthorize("hasAuthority('menu_wf_workflowdef')")
    public String codeRead(String businessKey) throws  Exception {
        InputStream resourceAsStream = modelService.xmlRead(businessKey,"xml");

        StringWriter writer = new StringWriter();
        IOUtils.copy(resourceAsStream, writer, StandardCharsets.UTF_8.name());
        String str = writer.toString();
        return str;

    }
    @Log("获取流程定义结点列表")
    @RequestMapping(value = "/authapi/wf_Model/getProcessActivities", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @PreAuthorize("hasAuthority('common')")
    public Collection<FlowElement> getProcessActivities(String key) throws  Exception {
       return  modelService.getProcessActivities(key);
    }
    @Log("获取流程定义列表")
    @RequestMapping(value = "/authapi/wf_Model/getMadelList", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Model> getMadelList() throws  Exception {
        return  modelService.getMadelList();
    }
}