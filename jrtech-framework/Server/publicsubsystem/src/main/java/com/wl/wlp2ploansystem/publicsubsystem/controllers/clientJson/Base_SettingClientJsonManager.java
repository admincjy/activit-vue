package com.wl.wlp2ploansystem.publicsubsystem.controllers.clientJson;

import com.wl.wlp2ploansystem.infrastructures.common.clientJson.ClientJsonManager;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Setting;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class Base_SettingClientJsonManager implements ClientJsonManager {

    @Autowired
    private Base_SettingService service;
    public Base_SettingClientJsonManager() {
    }

    @Override
    public Map<String, Object> getClientJsonObject() {
        Collection<Base_Setting> settings = service.getAll();

        return Collections.singletonMap("base_setting",settings);
    }
}
