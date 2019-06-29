package com.wl.wlp2ploansystem.infrastructures.common.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;

/**
 * 默认消息通知服务接口，web socket 实现
 */
@Component
public class NotificationServiceImpl implements NotificationSevice {
    @Autowired
    public SimpMessagingTemplate template;

    public void notify(String user,Object payload){
        String destination="/notification";
        template.convertAndSendToUser(user,destination,payload);
    }
    public  void nofityAll(Object payload){
        template.convertAndSend("/topic/getResponse", payload);
    }


    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public Exception handleExceptions(Exception t){
        t.printStackTrace();
        return t;
    }
}
