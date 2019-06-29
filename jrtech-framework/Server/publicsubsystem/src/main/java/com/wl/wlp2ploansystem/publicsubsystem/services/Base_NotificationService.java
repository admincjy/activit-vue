package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Notification;

import java.util.List;

public interface Base_NotificationService {
    List<Base_Notification> getList(EntityWrapper<Base_Notification> ew);
    Page<Base_Notification> getPagedList(Page<Base_Notification> pager, EntityWrapper<Base_Notification> ew);
    Base_Notification get(String id);
    String save(Base_Notification input);
    void saveAndNotify( Base_Notification input);
    void read(List<String> ids);
    void readBySource(String source);
    void readAll();
    void delete(String id);
    void batchDelete(List<String> ids);
    Integer count(EntityWrapper<Base_Notification> ew);
    Integer getNotificationCount();
}
