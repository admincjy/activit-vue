package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DBDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_DBDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_DBDictionary",produces = {MediaType.APPLICATION_JSON_VALUE})
@Display("数据库字典服务")
public class Base_DBDictionaryController {

    @Autowired
    private Base_DBDictionaryService service;


    @PostMapping("/queryDBDictionary")
    @Display("获取数据库字典")
    @Log("获取数据库字典")
    @PreAuthorize("hasAuthority('menu_base_dbdictionary')")
    public List<Base_DBDictionary> queryDBDictionary() throws  SQLException{

            return service.queryDBDictionary();

    }
}
