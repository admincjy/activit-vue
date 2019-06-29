package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.aspose.words.IMailMergeDataSource;
import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.support.FileHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.HttpHelper;
import com.wl.wlp2ploansystem.infrastructures.common.templateExport.TemplateExportStrategy;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDoc;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterDeleteInDto;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterSaveInDto;
import com.wl.wlp2ploansystem.loansubsystem.servicesimpl.templateExport.PL_LoanEnterTemplateExportDataSource;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ExportTemplate;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ExportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/authapi/pl_loanenter",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_LoanEnterController {

    @Autowired
    private PL_LoanEnterService service;

    @Autowired
    private PL_LoanDocService docService;

    @Autowired
    private Base_ExportTemplateService exportTemplateService;

    @Autowired
    private TemplateExportStrategy templateExportStrategy;

    @Autowired
    ApplicationProperties applicationProperties;

    @PostMapping("/get")
    @Log("读取借款单据信息(id)")
    public Object get(String id){
        return service.get(id);
    }

    @PostMapping("/save")
    @Log("新增/修改借款单据信息")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyInputList')")
    public Object save(@RequestBody PL_LoanEnterSaveInDto inputDto) throws IOException{
        String id = service.save(inputDto);

        return  this.get(id);
    }
    @PostMapping("/submit")
    @Log("提交借款单据信息")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyInputList')")
    public Map<String,String> submit(@RequestBody PL_LoanEnterSaveInDto inputDto) throws IOException {
        return service.submit(inputDto);
    }
    @PostMapping("/delete")
    @Log("删除借款单据信息")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyInputList')")
    public void delete(@RequestBody PL_LoanEnterDeleteInDto inputDto) {
        service.delete(inputDto);
    }

    @GetMapping("/export")
    @Log("导出合同")
    @PreAuthorize("hasAuthority('menu_pl_loanapplyInputList')")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String loanDocId = request.getParameter("loanDocId");
        String templateId = request.getParameter("templateId");

        PL_LoanDoc loanDoc = docService.get(loanDocId);
        Base_ExportTemplate exportTemplateModel = exportTemplateService.get(templateId);


        List<IMailMergeDataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(new PL_LoanEnterTemplateExportDataSource(loanDocId));

        byte[] pdf =  templateExportStrategy.exportWord(exportTemplateModel.getPath(),dataSourceList);
        String filename = loanDoc.getCustomerName() + "-" +  exportTemplateModel.getName() ;

        HttpHelper.download(request,response,filename,pdf);

    }

    @GetMapping("/batchExport")
    @Log("批量导出合同")
    public void batchExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String loanDocId = request.getParameter("loanDocId");
        String templateIds = request.getParameter("templateId");
        String expertDir = FileHelper.combarePath(applicationProperties.getFileupload().getFilePath(),"batchExport",loanDocId);

        Map<byte[],String> fileMap = new HashMap<byte[],String>();
        String[] templateIdarr = templateIds.split(",");
        PL_LoanDoc loanDoc = docService.get(loanDocId);

        List<IMailMergeDataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(new PL_LoanEnterTemplateExportDataSource(loanDocId));

        for(String templateId : templateIdarr){
            Base_ExportTemplate exportTemplateModel = exportTemplateService.get(templateId);
            byte[] pdf =  templateExportStrategy.exportWord(exportTemplateModel.getPath(),dataSourceList);
            String filename = loanDoc.getCustomerName() + "-" +  exportTemplateModel.getName() ;
            fileMap.put(pdf,filename);
        }

        FileHelper.makeDirectory(expertDir);
        FileHelper.emptyDirectory(expertDir);
        String zipPath = FileHelper.combarePath(expertDir, loanDoc.getCustomerName() + "-进件文件.zip");


        FileHelper.batchZip(fileMap,zipPath);
        String outzipname = loanDoc.getCustomerName() + "-进件文件.zip";
        HttpHelper.download(request,response,outzipname,zipPath);

    }
}
