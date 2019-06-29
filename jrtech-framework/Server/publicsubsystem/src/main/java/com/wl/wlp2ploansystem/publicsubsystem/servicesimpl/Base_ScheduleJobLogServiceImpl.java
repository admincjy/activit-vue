package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.schedule.ScheduleQuartzJobDetail;
import com.wl.wlp2ploansystem.infrastructures.common.schedule.ScheduleQuartzJobLogService;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobLog;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_ScheduleJobLogRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ScheduleJobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Base_ScheduleJobLogServiceImpl extends BaseServiceImpl implements Base_ScheduleJobLogService,ScheduleQuartzJobLogService {

    protected static final Logger logger = LoggerFactory.getLogger(Base_LogServiceImpl.class);

    @Autowired
    private Base_ScheduleJobLogRepository repository;

    @Transactional(readOnly = true)
    public Page<Base_ScheduleJobLog> getPagedList(Page<Base_ScheduleJobLog> page, EntityWrapper<Base_ScheduleJobLog> ew) {

        return page.setRecords(repository.selectPage(page,ew));

    }

    @Override
    @Transactional(readOnly = true)
    public Base_ScheduleJobLog get(String id) {
        Base_ScheduleJobLog entity = repository.selectById(id);

        return entity;
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
    public void insert(Base_ScheduleJobLog input) {

        input.setId(IdGenerator.get());
        repository.insert(input);

    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void startExecute(ScheduleQuartzJobDetail scheduleQuartzJobDetail) {
        Base_ScheduleJobLog log = new Base_ScheduleJobLog();
        log.setId(scheduleQuartzJobDetail.getExecuteId());
        log.setJobId(scheduleQuartzJobDetail.getId());
        log.setJobName(scheduleQuartzJobDetail.getName());
        log.setBeanName(scheduleQuartzJobDetail.getBeanName());
        log.setMethodName(scheduleQuartzJobDetail.getMethodName());
        log.setParams(scheduleQuartzJobDetail.getParams());
        log.setResult(scheduleQuartzJobDetail.getResult());
        log.setErrorStackTrace(scheduleQuartzJobDetail.getErrorStackTrace());
        log.setExecuteTime(scheduleQuartzJobDetail.getExecuteTime());
        log.setDuration(scheduleQuartzJobDetail.getDuration());
        log.setGmtCreatedOn(scheduleQuartzJobDetail.getGmtCreatedOn());
        try {
            repository.insertAllColumn(log);
        }
        catch (Exception ex){
            logger.error("日志记录方法失败",ex);
            throw ex;
        }
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void endExecute(ScheduleQuartzJobDetail scheduleQuartzJobDetail) {
        Base_ScheduleJobLog log = new Base_ScheduleJobLog();
        log.setId(scheduleQuartzJobDetail.getExecuteId());
        log.setJobId(scheduleQuartzJobDetail.getId());
        log.setJobName(scheduleQuartzJobDetail.getName());
        log.setBeanName(scheduleQuartzJobDetail.getBeanName());
        log.setMethodName(scheduleQuartzJobDetail.getMethodName());
        log.setParams(scheduleQuartzJobDetail.getParams());
        log.setResult(scheduleQuartzJobDetail.getResult());
        log.setErrorStackTrace(scheduleQuartzJobDetail.getErrorStackTrace());
        log.setExecuteTime(scheduleQuartzJobDetail.getExecuteTime());
        log.setDuration(scheduleQuartzJobDetail.getDuration());
        log.setGmtVersion(0);
        log.setGmtCreatedOn(scheduleQuartzJobDetail.getGmtCreatedOn());

        try {
            repository.updateAllColumnById(log);
        }
        catch (Exception ex){
            logger.error("日志记录方法失败",ex);
            throw ex;
        }
    }
}