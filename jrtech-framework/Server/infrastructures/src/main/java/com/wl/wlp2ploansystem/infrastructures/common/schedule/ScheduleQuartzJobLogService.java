package com.wl.wlp2ploansystem.infrastructures.common.schedule;

/**
 * 定时任务日志记录服务接口
 *
 */
public interface ScheduleQuartzJobLogService {
    /**
     * 开始执行
     */
    void startExecute(ScheduleQuartzJobDetail scheduleQuartzJobDetail);

    /**
     * 执行完成
     */
    void endExecute(ScheduleQuartzJobDetail scheduleQuartzJobDetail);
}
