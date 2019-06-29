package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.schedule.ScheduleQuartzJobDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_schedule_job")
public class Base_ScheduleJob extends FullAuditedEntity {

    @NotBlank(message="名称不能为空")
    private String name;
    /**
     * spring bean名称
     */
    @NotBlank(message="bean名称不能为空")
    private String beanName;

    /**
     * 方法名
     */
    @NotBlank(message="方法名称不能为空")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    @NotBlank(message="cron表达式不能为空")
    private String cronExpression;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    public ScheduleQuartzJobDetail toScheduleQuartzJobDetail() {

        ScheduleQuartzJobDetail detail = new ScheduleQuartzJobDetail();
        detail.setId(this.getId());
        detail.setName(this.getName());
        detail.setBeanName(this.getBeanName());
        detail.setMethodName(this.getMethodName());
        detail.setParams(this.getParams());
        detail.setCronExpression(this.getCronExpression());

        return  detail;
    }
}
