package com.wl.wlp2ploansystem.infrastructures.common.schedule;

import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.infrastructures.common.support.TypeHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务
 *
 */
public class ScheduleQuartzJob extends QuartzJobBean {
    /**
     * 任务调度参数key
     */

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
       Object ScheduleQuartzJobDetailobj =  context.getMergedJobDataMap()
                .get(ScheduleQuartzJobDetail.JOB_PARAM_KEY);

        ScheduleQuartzJobLogService scheduleJobLogService = SpringContextUtil.getBean(ScheduleQuartzJobLogService.class);
        ScheduleQuartzJobDetail scheduleJob = (ScheduleQuartzJobDetail) ScheduleQuartzJobDetailobj;

        //任务开始时间
        long startTime = System.currentTimeMillis();

        try {
            scheduleJob.setExecuteId(IdGenerator.get());
            scheduleJob.setResult(ScheduleQuartzJobDetail.JOB_RESULT_EXECUTING);
            scheduleJob.setGmtCreatedOn(LocalDateTime.now());
            scheduleJobLogService.startExecute(scheduleJob);
            //执行任务
            logger.info("任务准备执行，任务ID：" + scheduleJob.getId());
            ScheduleQuartzRunnable task = new ScheduleQuartzRunnable(scheduleJob.getBeanName(),
                    scheduleJob.getMethodName(), scheduleJob.getParams());
            Future<?> future = service.submit(task);

            future.get();

            //任务状态    0：成功    1：失败
            scheduleJob.setResult(ScheduleQuartzJobDetail.JOB_RESULT_SUCCESS);

            logger.info("任务执行完毕，任务ID：" + scheduleJob.getId() );
        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：" + scheduleJob.getId(), e);


            scheduleJob.setResult(ScheduleQuartzJobDetail.JOB_RESULT_ERROR);

            int messageLength = Math.min(e.getMessage().length(),1000);
            String errorMessage = StringUtils.isEmpty(e.getMessage())?null:e.getMessage().substring(0,messageLength);
            scheduleJob.setErrorMessage(errorMessage);

            String stackTraceFullMessage = TypeHelper.getExcetionStackTrace(e);
            int stackTraceLength = Math.min(stackTraceFullMessage.length(),4000);
            String stackTrace= StringUtils.isEmpty(stackTraceFullMessage)?null:stackTraceFullMessage.substring(0,stackTraceLength);
            scheduleJob.setErrorStackTrace(stackTrace);
        }finally {
            //任务执行总时长
            long duration = System.currentTimeMillis() - startTime;
            scheduleJob.setDuration(duration);
            scheduleJob.setExecuteTime(LocalDateTime.now());

            scheduleJobLogService.endExecute(scheduleJob);
        }
    }
}