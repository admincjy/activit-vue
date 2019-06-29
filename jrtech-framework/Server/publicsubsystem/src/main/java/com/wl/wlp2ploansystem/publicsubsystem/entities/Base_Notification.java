package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_notification")
public class Base_Notification extends FullAuditedEntity {

    public   static  final String targetType_all = "all";
    @NotBlank
    private String title;

    private String content;

    private String url;
    
    //
    private String source;

    private String targetType = targetType_all;
    private String targetTos;

    private  boolean read;

    private LocalDateTime readDate;
}

