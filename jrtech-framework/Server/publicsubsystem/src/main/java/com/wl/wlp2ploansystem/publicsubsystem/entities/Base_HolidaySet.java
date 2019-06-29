package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_holiday_set")
public class Base_HolidaySet extends FullAuditedEntity {

    @Display("日期")
    private LocalDate cdate;

    @Display("是否假期")
    private Boolean wk;

}
