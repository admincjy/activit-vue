package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_schedule_job_log")
public class Base_ScheduleJobLog extends FullAuditedEntity {


    /**
     * 任务id
     */
    private String jobId;

    @NotBlank(message="名称不能为空")
    private String jobName;

    /**
     * spring bean名称
     */
    private String beanName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务状态
     */
    private String result;

    //执行时长(毫秒)
    private Long duration;

    private LocalDateTime executeTime;

    private String errorMessage;

    private String errorStackTrace;
}
