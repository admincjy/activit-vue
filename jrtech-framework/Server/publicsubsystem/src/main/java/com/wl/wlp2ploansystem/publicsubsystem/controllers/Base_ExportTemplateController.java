package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ExportTemplate;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ExportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_exporttemplate",produces = {MediaType.APPLICATION_JSON_VALUE})

public class Base_ExportTemplateController {
    @Autowired
    private Base_ExportTemplateService service;

    @PostMapping("/getList")
    @Log("读取导出模板列表")
    @PreAuthorize("hasAuthority('menu_base_exporttemplate')")
    public List<Base_ExportTemplate> getList( String  templateCategoryId) {
        EntityWrapper<Base_ExportTemplate> ew = new EntityWrapper<Base_ExportTemplate>();
        ew.orderBy("gmtCreatedOn", false);
        ew.eq("templateCategoryId",templateCategoryId);

        List<Base_ExportTemplate> results = service.getList(ew);

        return results;

    }

    @PostMapping("/get")
    @Log("读取导出模板信息")
    @PreAuthorize("hasAuthority('menu_base_exporttemplate')")
    public Base_ExportTemplate get(String id) {
        return service.get(id);
    }
    @PostMapping("/save")
    @Log("新增/修改导出模板信息")
    @PreAuthorize("hasAuthority('menu_base_exporttemplate')")
    public HashMap<String,String> save(@RequestBody  Base_ExportTemplate input) {
        String id =  service.save(input);

        HashMap<String,String> result = new HashMap<String,String>();
        result.put("id",id);

        return result;
    }
    @PostMapping("/delete")
    @Log("删除导出模板信息")
    @PreAuthorize("hasAuthority('menu_base_exporttemplate')")
    public void delete(String id) {
        service.delete(id);
    }
    @PostMapping("/batchDelete")
    @Log("批量删除导出模板信息")
    @PreAuthorize("hasAuthority('menu_base_exporttemplate')")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}