package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_v_schedule_job_full")
public class Base_ScheduleJobFull extends Base_ScheduleJob{

    /**
     * 最后任务状态
     */
    private String lastLogResult;

    /**
     * 最后任务执行时间
     */
    private LocalDateTime lastLogExecuteTime;

}
