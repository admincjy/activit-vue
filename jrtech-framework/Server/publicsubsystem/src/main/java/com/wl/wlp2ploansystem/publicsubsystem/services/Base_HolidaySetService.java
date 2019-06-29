package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_HolidaySet;

import java.time.LocalDate;
import java.util.List;

public interface Base_HolidaySetService extends BaseService {

    List<Base_HolidaySet> systemGenerateList(int year);
    List<Base_HolidaySet> getListByYear(int year);
    void saveList(List<Base_HolidaySet> modelList);

    List<LocalDate> getNextNonWKDates(LocalDate localDate, int count);

    Base_HolidaySet get(String id);

    String save(Base_HolidaySet input);

    void delete(String id);

    void batchDelete(List<String> ids);

    Page<Base_HolidaySet> getPagedList(Page<Base_HolidaySet> pager, EntityWrapper<Base_HolidaySet> ew);
}
