package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedType;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Setting;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_SettingRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class Base_SettingServiceImpl extends BaseServiceImpl implements Base_SettingService {
    @Autowired
    Base_SettingRepository repository;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Collection<Base_Setting> getAll() {
        return  repository.selectList(null);
    }
    @Override
    public  void save(Base_Setting input){
        Base_Setting dbEntity = repository.selectById(input.getId());
        dbEntity.setValue(input.getValue());
        Integer effectRecords= repository.updateById(dbEntity);

        if(effectRecords == 0){
            throw new OptimisticConcurrencyException();
        }
        applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

        applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
    }
    @Override
    @Transactional
    public  void saveList(Collection<Base_Setting> input){
        input.forEach(p->{
            save(p);
        });
        applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

        applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
    }
    @Override
    public  String getStringValue(String id){
        Base_Setting setting = this.get(id);
        if(setting == null){
            return null;
        }
        return  setting.getValue();
    }
    @Override
    public  Boolean getBooleanValue(String id){
        Base_Setting setting = this.get(id);
        if(setting == null){
            return null;
        }
        return  Boolean.valueOf(setting.getValue());
    }
    @Override
    public  Base_Setting get(String id){
        return repository.selectById(id);
    }
    @Override
    public  Integer getIntegerValue(String id){
        Base_Setting setting = this.get(id);
        if(setting == null){
            return null;
        }
        return  Integer.valueOf(setting.getValue());
    }
}
