package com.wl.wlp2ploansystem.publicsubsystem.config;

import com.wl.wlp2ploansystem.infrastructures.common.ueditor.UEditorConfigurer;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * UEditor 配置器
 */

@Component
public class WLUEditorConfigurer implements UEditorConfigurer {

    @Autowired
    private Base_SettingService settingService;
    /***
     * 覆盖配置文件中fileAllowFiles的设置
     * @return
     */
    @Override
    public String[] getUploadFileAllowFiles() {

        String  allowFiles =  settingService.getStringValue("Attachment_Accept_Extensions");
        return  allowFiles.split(",");
    }
}
