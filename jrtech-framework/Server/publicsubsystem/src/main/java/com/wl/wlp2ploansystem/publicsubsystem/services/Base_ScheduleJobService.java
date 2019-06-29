package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJob;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobFull;

public interface Base_ScheduleJobService extends BaseService {
    Page<Base_ScheduleJobFull> getPagedList(Page<Base_ScheduleJobFull> page, EntityWrapper<Base_ScheduleJobFull> ew);
    Base_ScheduleJob get(String id);
    String save(Base_ScheduleJob scheduleJob);
    void batchDelete(String[] jobIds);
    void run(String[] jobIds);
    void pause(String[] jobIds);
    void resume(String[] jobIds);
}
