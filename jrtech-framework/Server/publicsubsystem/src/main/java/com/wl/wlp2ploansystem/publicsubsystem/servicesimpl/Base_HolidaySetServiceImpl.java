package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_HolidaySet;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_HolidaySetRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_HolidaySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Base_HolidaySetServiceImpl extends BaseServiceImpl implements Base_HolidaySetService {


    @Autowired
    private Base_HolidaySetRepository repository;


    @Transactional(readOnly = true)
    public Page<Base_HolidaySet> getPagedList(Page<Base_HolidaySet> page, EntityWrapper<Base_HolidaySet> ew) {

        if (null != ew) {
            ew.orderBy(page.getOrderByField(), page.isAsc());
        }
        return page.setRecords(repository.selectPage(page, ew));

    }

    @Override
    @Transactional(readOnly = true)
    public Base_HolidaySet get(String id) {
        Base_HolidaySet entity = repository.selectById(id);
        return entity;
    }
    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getNextNonWKDates(LocalDate localDate,int count){
        EntityWrapper<Base_HolidaySet> entityWrapper = new EntityWrapper<>();
        entityWrapper.ge("cdate",localDate);
        entityWrapper.eq("wk",Boolean.FALSE);
        entityWrapper.orderBy("cdate",true);
        entityWrapper.last(" limit 0," + count);

        return  repository.selectList(entityWrapper).stream().map(p->p.getCdate()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String save(Base_HolidaySet input ) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            repository.insert(input);
        } else {
            repository.updateAllColumnById(input);
        }
        return input.getId();
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

    @Override
    @Transactional
    public List<Base_HolidaySet> systemGenerateList(int year) {
        LocalDate beginDate = LocalDate.of(year,1,1);
        int totalDays = beginDate.lengthOfYear();

        List<Base_HolidaySet> modelList = new ArrayList<Base_HolidaySet>();
        for (int i = 0; i < totalDays; i++)
        {
            LocalDate dt = beginDate.plusDays(i);

            Base_HolidaySet model = new Base_HolidaySet();
            model.setCdate(dt);
            model.setWk(dt.getDayOfWeek() == DayOfWeek.SATURDAY || dt.getDayOfWeek() == DayOfWeek.SUNDAY?Boolean.TRUE:Boolean.FALSE);
            modelList.add(model);
        }
        saveList(modelList);

        return modelList;

    }
    @Override
    @Transactional(readOnly = true)
    public List<Base_HolidaySet> getListByYear(int year){

        EntityWrapper<Base_HolidaySet> entityWrapper = new EntityWrapper<>();
        entityWrapper.ge("cdate",LocalDate.of(year,1,1));
        entityWrapper.le("cdate",LocalDate.of(year,12,31));

        List<Base_HolidaySet> list = repository.selectList(entityWrapper);

        return  list;
    }

    @Override
    @Transactional
    public void saveList(List<Base_HolidaySet> modelList) {
        if (modelList.size() == 0)
        {
            throw new UserFriendlyException("没有需要保存的数据！");
        }
        int year = modelList.get(0).getCdate().getYear();

        EntityWrapper<Base_HolidaySet> entityWrapper = new EntityWrapper<>();
        entityWrapper.ge("cdate",LocalDate.of(year,1,1));
        entityWrapper.le("cdate",LocalDate.of(year,12,31));
        repository.delete(entityWrapper);
        modelList.forEach(p->{
            p.setId(IdGenerator.get());
            repository.insert(p);
        });
    }
}