package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileParam;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileUploadUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.HttpHelper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ExportTemplate;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ExportTemplateService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class Base_ExportTemplateManagementController extends HttpServlet {

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    Base_ExportTemplateService exportTemplateService;

    @Autowired
    Base_SettingService settingService;


    @PostMapping("/authapi/base_exportTemplateManagement/upload")
    @PreAuthorize("hasAuthority('common')")
    public void upload(HttpServletRequest request) throws Exception {

        MultipartFileParam param = MultipartFileUploadUtils.parse(request);
        if (!param.isMultipart()) {
            return;
        }
        String fileAllowedExtentionValues = settingService.getStringValue("Attachment_Accept_Extensions");

        String globalDirPath = applicationProperties.getFileupload().getFilePath();
        String fileExten = param.getFileExtension();
        String fid = param.getFid();
        String businessDocId= param.getBusinessDocId();
        String attachmentCategoryId= param.getAttachmentCategoryId();
        String fileName = param.getFileName();

        HttpHelper.multipartFileUpload(request,param,fileAllowedExtentionValues,globalDirPath,p->{
            Base_ExportTemplate fielModel = new Base_ExportTemplate();
            fielModel.setId(fid);
            fielModel.setName(param.getFileName());
            fielModel.setTemplateCategoryId(attachmentCategoryId);
            fielModel.setPath(p);
            exportTemplateService.save(fielModel);
        });

    }

    @GetMapping("/authapi/base_exportTemplateManagement/download")
    @PreAuthorize("hasAuthority('common')")
    public void download(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Base_ExportTemplate attachmentModel = exportTemplateService.get(id);
        String filename = attachmentModel.getName();
        HttpHelper.download(request,response,filename,attachmentModel.getPath());


    }
}
