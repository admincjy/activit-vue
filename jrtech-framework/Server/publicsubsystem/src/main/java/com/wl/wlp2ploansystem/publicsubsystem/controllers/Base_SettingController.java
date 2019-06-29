package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Setting;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/authapi/base_Setting")
public class Base_SettingController {
    @Autowired
    private Base_SettingService service;

    @PostMapping("/saveList")
    @Log("修改配置项")
    @PreAuthorize("hasAuthority('menu_base_setting')")
    public void saveList(@RequestBody Collection<Base_Setting> input) {
        service.saveList(input);
    }
}
