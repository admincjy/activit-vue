package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.schedule.ScheduleQuartzJobDetail;
import com.wl.wlp2ploansystem.infrastructures.common.schedule.ScheduleUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJob;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ScheduleJobFull;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_ScheduleJobFullRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_ScheduleJobRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ScheduleJobService;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service("scheduleJobService")
public class Base_ScheduleJobServiceImpl extends BaseServiceImpl implements Base_ScheduleJobService {
    @Autowired
    private Base_ScheduleJobRepository repository;
    @Autowired
    private Base_ScheduleJobFullRepository fullRepository;
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init(){

        List<Base_ScheduleJob> scheduleJobList = repository.selectList(null);
        for(Base_ScheduleJob scheduleJob : scheduleJobList){
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());

            ScheduleQuartzJobDetail scheduleQuartzJobDetail = scheduleJob.toScheduleQuartzJobDetail();
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleQuartzJobDetail);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleQuartzJobDetail);
            }
        }

    }

    @Override
    public Page<Base_ScheduleJobFull> getPagedList(Page<Base_ScheduleJobFull> page, EntityWrapper<Base_ScheduleJobFull> ew) {
        return page.setRecords(fullRepository.selectPage(page, ew));
    }

    @Override
    @Transactional(readOnly = true)
    public Base_ScheduleJob get(String id) {
        Base_ScheduleJob entity = repository.selectById(id);

        return entity;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(Base_ScheduleJob scheduleJob) {
        if (StringUtils.isEmpty(scheduleJob.getId())) {
            scheduleJob.setId(IdGenerator.get());
            scheduleJob.setStatus(ScheduleQuartzJobDetail.JOB_STATUS_NORMAL);
            repository.insert(scheduleJob);

            ScheduleUtils.createScheduleJob(scheduler, scheduleJob.toScheduleQuartzJobDetail());
        } else {
            ScheduleUtils.updateScheduleJob(scheduler, scheduleJob.toScheduleQuartzJobDetail());

            repository.updateAllColumnById(scheduleJob);
        }

        return  scheduleJob.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String[] jobIds) {
        for(String jobId : jobIds){
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
        repository.deleteBatchIds(Arrays.asList(jobIds));
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String[] jobIds) {
        for(String jobId : jobIds){
            Base_ScheduleJob job = this.get(jobId);
            ScheduleUtils.run(scheduler, job.toScheduleQuartzJobDetail());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(String[] jobIds) {
        for(String jobId : jobIds){
            ScheduleUtils.pauseJob(scheduler, jobId);
            Base_ScheduleJob job = this.get(jobId);
            if(ScheduleQuartzJobDetail.JOB_STATUS_NORMAL.equals(job.getStatus())) {
                job.setStatus(ScheduleQuartzJobDetail.JOB_STATUS_PAUSE);

                Integer effectRecords= repository.updateById(job);

                if(effectRecords == 0){
                    throw new OptimisticConcurrencyException();
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(String[] jobIds) {
        for(String jobId : jobIds){
            ScheduleUtils.resumeJob(scheduler, jobId);
            Base_ScheduleJob job = this.get(jobId);
            if(ScheduleQuartzJobDetail.JOB_STATUS_PAUSE.equals(job.getStatus())) {
                job.setStatus(ScheduleQuartzJobDetail.JOB_STATUS_NORMAL);
                Integer effectRecords= repository.updateById(job);

                if(effectRecords == 0){
                    throw new OptimisticConcurrencyException();
                }
            }
        }

    }

}
