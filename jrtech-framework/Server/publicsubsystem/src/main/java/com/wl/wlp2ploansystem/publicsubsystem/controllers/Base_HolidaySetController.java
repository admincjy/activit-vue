package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_HolidaySet;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_HolidaySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_holidayset",produces = {MediaType.APPLICATION_JSON_VALUE})

public class Base_HolidaySetController {
    @Autowired
    private Base_HolidaySetService service;

    @PostMapping("/getListByYear")
    @Log("读取年内假期设置")
    @PreAuthorize("hasAuthority('menu_base_holidayset')")
    public List<Base_HolidaySet> getListByYear(Integer year) {

        return  service.getListByYear(year.intValue());

    }
    @PostMapping("/saveList")
    @Log("新增年内假期设置")
    @PreAuthorize("hasAuthority('menu_base_holidayset')")
    public void saveList(@RequestBody List<Base_HolidaySet> input) {
        service.saveList(input);
    }
    @PostMapping("/systemGenerateList")
    @Log("年内系统生成假期设置")
    @PreAuthorize("hasAuthority('menu_base_holidayset')")
    public List<Base_HolidaySet> systemGenerateList(Integer year) {
        return  service.systemGenerateList(year.intValue());
    }


}
