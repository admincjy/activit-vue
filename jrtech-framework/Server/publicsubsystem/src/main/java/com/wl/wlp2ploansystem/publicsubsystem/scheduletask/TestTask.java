package com.wl.wlp2ploansystem.publicsubsystem.scheduletask;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("testTask")
public class TestTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Base_UserService sysUserService;

    public void test(String params){
        logger.info("我是带参数的test方法，正在被执行，参数为：" + params);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Base_User user = sysUserService.get("2");
        System.out.println(ToStringBuilder.reflectionToString(user));

    }


    public void test2(){
        logger.info("我是不带参数的test2方法，正在被执行");
    }
}
