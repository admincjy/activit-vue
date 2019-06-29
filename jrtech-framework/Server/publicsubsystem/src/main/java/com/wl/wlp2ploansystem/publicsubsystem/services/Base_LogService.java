package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Log;

import java.util.List;

public interface Base_LogService {

    Page<Base_Log> getPagedList(Page<Base_Log> pager, EntityWrapper<Base_Log> ew);

    void insert(Base_Log entity);

    Base_Log get(String id);

    void delete(String id);

    void batchDelete(List<String> ids);
}
