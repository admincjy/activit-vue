package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.notification.NotificationSevice;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Notification;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_NotificationRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class Base_NotificationServiceImpl implements Base_NotificationService {

    @Autowired
    private Base_NotificationRepository repository;

    @Autowired
    private NotificationSevice notificationSevice;

    @Transactional(readOnly = true)
    public Page<Base_Notification> getPagedList(Page<Base_Notification> page, EntityWrapper<Base_Notification> ew) {

        return page.setRecords(repository.selectPage(page, ew));

    }
    @Override
    @Transactional(readOnly=true)
    public Integer count(EntityWrapper<Base_Notification> ew) {

        return repository.selectCount(ew);

    }
    @Override
    @Transactional(readOnly=true)
    public Integer getNotificationCount() {

        EntityWrapper<Base_Notification> ew = new EntityWrapper<Base_Notification>();

        ew.eq("`read`","0");
        ew.andNew().like("targetTos",","+ SecurityUtils.getCurrentUser().getId()+",")
                .or().like("targetTos",SecurityUtils.getCurrentUser().getId()+",", SqlLike.RIGHT)
                .or().like("targetTos",","+ SecurityUtils.getCurrentUser().getId(), SqlLike.LEFT)
                .or().eq("targetType",Base_Notification.targetType_all);

        return  repository.selectCount(ew);

    }
    @Override
    @Transactional(readOnly=true)
    public List<Base_Notification> getList(EntityWrapper<Base_Notification> ew) {

        if (null != ew) {
            ew.orderBy("id", true);
        }
        return repository.selectList(ew);

    }

    @Override
    public Base_Notification get(String id) {
        return  repository.selectById(id);
    }

    @Override
    @Transactional
    public String save(Base_Notification input) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            repository.insert(input);
        } else {
            Integer effectRecords= repository.updateAllColumnById(input);

            if(effectRecords == 0){
                throw new OptimisticConcurrencyException();
            }
        }
        return input.getId();
    }
    @Override
    @Transactional
    public void saveAndNotify( Base_Notification input) {
        String id =  this.save(input);

        EntityWrapper<Base_Notification> ew = new EntityWrapper<Base_Notification>();
        ew.eq("`read`","0");
        int noReadCount = this.count(ew);
        HashMap<String,String> response = new HashMap<>();
        response.put("title",input.getTitle());
        response.put("content",input.getContent());
        response.put("noReadCount",String.valueOf(noReadCount));
        if(Base_Notification.targetType_all.equals(input.getTargetType())) {
            notificationSevice.nofityAll(response);
        }else{
            String[] targetToArray =  input.getTargetTos().split(",");
            for(String targetTo:targetToArray ){
                if(StringUtils.isEmpty(targetTo)){
                    continue;
                }
                notificationSevice.notify(targetTo,response);
            }

        }

    }
    @Override
    @Transactional
    public void readBySource(String source) {

        EntityWrapper<Base_Notification> ew = new EntityWrapper<Base_Notification>();

        ew.eq("`read`","0");
        ew.eq("`source`",source);

        Base_Notification saveEntity = new Base_Notification();
        saveEntity.setRead(true);
        saveEntity.setReadDate(LocalDateTime.now());


        repository.update(saveEntity,ew);
    }
    @Override
    @Transactional
    public void read(List<String> ids) {

        EntityWrapper<Base_Notification> ew = new EntityWrapper<Base_Notification>();

        ew.eq("`read`","0");
        ew.in("`id`",ids);

        Base_Notification saveEntity = new Base_Notification();
        saveEntity.setRead(true);
        saveEntity.setReadDate(LocalDateTime.now());


        repository.update(saveEntity,ew);
    }
    @Override
    @Transactional
    public void readAll() {

        Base_Notification saveEntity = new Base_Notification();
        saveEntity.setRead(true);
        saveEntity.setReadDate(LocalDateTime.now());
        repository.update(saveEntity,null);

    }
    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }
    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        ids.forEach(p -> {
            delete(p);
        });
    }

}
