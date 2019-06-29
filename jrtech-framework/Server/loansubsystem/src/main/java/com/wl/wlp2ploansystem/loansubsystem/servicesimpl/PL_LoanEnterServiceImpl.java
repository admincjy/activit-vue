package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_TaskActionDto;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDoc;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocFull;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterCompanyService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterGXDService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterCompanySaveInputDto;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterDeleteInDto;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterGXDSaveInDto;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterSaveInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_AttachmentService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SequenceNumberService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.WF_WorkflowStartInDto;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service("PL_LoanEnterService")
public class PL_LoanEnterServiceImpl extends BaseServiceImpl implements PL_LoanEnterService {

    @Autowired
    private  ObjectMapper mapper;
    @Autowired
    private PL_LoanDocService docService;
    @Autowired
    private Base_AttachmentService attachmentService;

    @Autowired
    private Base_SequenceNumberService sequenceNumberService;

    @Autowired
    private WF_WorkflowService workflowService;



    @Transactional(readOnly = true)
    public Object get(String id)
    {
        PL_LoanDoc basicDocEntity = docService.get(id);
        String formTemplateTypeId = basicDocEntity.getFormTemplateTypeId();
        if("pl_formtemplatetype_gxd".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterGXDService gxdDocService = (PL_LoanEnterGXDService)SpringContextUtil.getBean("PL_LoanEnterGXDService");
            return gxdDocService.getFull(id);
        }
        else  if("pl_formtemplatetype_company".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterCompanyService service = (PL_LoanEnterCompanyService)SpringContextUtil.getBean("PL_LoanEnterCompanyService");
            return service.getFull(id);
        }
        return null;
    }
    @Transactional
    public String save(PL_LoanEnterSaveInDto input) throws IOException
    {
        String jsonString = input.getJsonString();
        JsonNode entityJsonNode = mapper.readTree(jsonString);
        String formTemplateTypeId =entityJsonNode.path("formTemplateTypeId").asText();
        String inputId=entityJsonNode.path("id").asText();

        if("pl_formtemplatetype_gxd".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterGXDSaveInDto gxdDocEntity = mapper.readValue(jsonString,PL_LoanEnterGXDSaveInDto.class);
            PL_LoanEnterGXDService gxdDocService = (PL_LoanEnterGXDService)SpringContextUtil.getBean("PL_LoanEnterGXDService");
            String docId =  gxdDocService.save(gxdDocEntity);
        }else if("pl_formtemplatetype_company".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterCompanySaveInputDto gxdDocEntity = mapper.readValue(jsonString,PL_LoanEnterCompanySaveInputDto.class);
            PL_LoanEnterCompanyService gxdDocService = (PL_LoanEnterCompanyService)SpringContextUtil.getBean("PL_LoanEnterCompanyService");
            String docId =  gxdDocService.save(gxdDocEntity);
        }
        return  inputId;
    }
    @Transactional
    public Map<String,String> submit(PL_LoanEnterSaveInDto input)throws IOException
    {
        String jsonString = input.getJsonString();

        JsonNode inputJsonNode = mapper.readTree(jsonString);
        String sn = inputJsonNode.hasNonNull("sn")?inputJsonNode.path("sn").asText():null;
        String formTemplateTypeId =inputJsonNode.path("formTemplateTypeId").asText();
        String docId=inputJsonNode.path("id").asText();
        String remark = inputJsonNode.path("remark").asText();

        if(!StringUtils.isEmpty(docId)) {
            Boolean attachmentValidResult = attachmentService.IsCategoriesAllValid(docId, Arrays.asList("pl_loanapplyInput"));
            if (!attachmentValidResult) {
                throw new UserFriendlyException("附件尚未上传完整！");
            }
        }
        if("pl_formtemplatetype_gxd".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterGXDSaveInDto gxdDocFullEntity = mapper.readValue(jsonString,PL_LoanEnterGXDSaveInDto.class);
            PL_LoanEnterGXDService gxdDocService = (PL_LoanEnterGXDService)SpringContextUtil.getBean("PL_LoanEnterGXDService");
            docId = gxdDocService.save(gxdDocFullEntity);
        }else if("pl_formtemplatetype_company".equalsIgnoreCase(formTemplateTypeId)){
            PL_LoanEnterCompanySaveInputDto gxdDocEntity = mapper.readValue(jsonString,PL_LoanEnterCompanySaveInputDto.class);
            PL_LoanEnterCompanyService gxdDocService = (PL_LoanEnterCompanyService)SpringContextUtil.getBean("PL_LoanEnterCompanyService");
            docId =  gxdDocService.save(gxdDocEntity);
        }
        PL_LoanDocFull docFullEntity = docService.getFull(docId);
        PL_LoanDoc reloadDocEntity = docService.get(docId);

        if (StringUtils.isEmpty(sn)) { //submit

            String customerMR = workflowService.getRoleUser(PL_LoanDoc.loanApplyProcessDefinitionKey,"audit","1078894061260292098");

            Map<String, Object> workflowInput = new HashMap<String, Object>();
            workflowInput.put("customerMR", customerMR);

            WF_WorkflowStartInDto workflowStartInDto = new WF_WorkflowStartInDto(PL_LoanDoc.loanApplyProcessDefinitionKey,
                    SecurityUtils.getCurrentUser().getId(),
                    docFullEntity.getDocOwnDepartmentId(),
                    docId,
                    "进件流程：" + docId,
                    reloadDocEntity.getGmtCreatedOn() == null?LocalDateTime.now():reloadDocEntity.getGmtCreatedOn(),
                    remark,
                    workflowInput
                    );
            String processId = workflowService.startWorkflow(workflowStartInDto);
            reloadDocEntity.setCustomerMRId(customerMR);
            reloadDocEntity.setLoanApplySumbitDate(LocalDateTime.now());
            docService.save(reloadDocEntity);
        }else  { //resubmit
            WF_TaskActionDto resubmitDto = new WF_TaskActionDto();
            resubmitDto.setTaskId(sn);
            resubmitDto.setAction("approve");
            resubmitDto.setDocId(docId);
            resubmitDto.setTaskRemark(remark);
            workflowService.handleTask(resubmitDto);

            workflowService.setProcessInstStatus(docFullEntity.getProcessInstId(), EProcessInstStatus.Approving.name());
        }

        HashMap<String,String> result = new HashMap<>();
        result.put("id",reloadDocEntity.getId());
        result.put("customerCode",reloadDocEntity.getCustomerCode());
        result.put("getGmtVersion",String.valueOf(reloadDocEntity.getGmtVersion()));

        return  result;
    }

    @Transactional
    @Override
    public void delete(PL_LoanEnterDeleteInDto input)
    {
        String procInstId = input.getProcInstId();
        String id = input.getId();

        if(!org.springframework.util.StringUtils.isEmpty(procInstId)) {
            workflowService.deleteProcessInstance(procInstId, "咨询放弃");
        }
        docService.delete(id);

    }
}
