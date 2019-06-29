package com.wl.wlp2ploansystem.infrastructures.common.ueditor;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UEditorConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private  UEditorConfigurer editorConfigurer;

    @Bean
    @ConditionalOnMissingBean(ActionEnter.class)
    public ActionEnter actionEnter() {
        ActionEnter actionEnter = new ActionEnter(ConfigManager.getInstance(applicationProperties.getUeditor(),editorConfigurer));
        return actionEnter;
    }
}
