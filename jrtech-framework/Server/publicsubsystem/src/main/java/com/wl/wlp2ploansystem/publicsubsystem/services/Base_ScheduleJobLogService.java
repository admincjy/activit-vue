package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobLog;

import java.util.List;

public interface Base_ScheduleJobLogService extends BaseService {
    Page<Base_ScheduleJobLog> getPagedList(Page<Base_ScheduleJobLog> page, EntityWrapper<Base_ScheduleJobLog> ew);
    Base_ScheduleJobLog get(String id);
    void delete(String id);
    void batchDelete(List<String> ids);
    void insert(Base_ScheduleJobLog input);
}
