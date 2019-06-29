package com.wl.wlp2ploansystem;

import com.wl.wlp2ploansystem.infrastructures.common.cache.AppCacheManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private Environment env;

    @Autowired
    private AppCacheManger appCacheManger;
    @Override
    public void run(String... args) throws Exception {

        /*
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
       */

        //应用启动时，刷新缓存
        logger.info(">>>>>>>>>>>>>>>reload app cache start<<<<<<<<<<<<<");
        appCacheManger.reload();
        logger.info(">>>>>>>>>>>>>>>reload app cache end<<<<<<<<<<<<<");

        if (env.getActiveProfiles().length != 0) {
            logger.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        logger.info("Web application fully configured");
    }
}
