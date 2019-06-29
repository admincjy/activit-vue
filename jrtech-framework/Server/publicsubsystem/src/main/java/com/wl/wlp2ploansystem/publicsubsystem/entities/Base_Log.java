package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_log")
public class Base_Log  extends FullAuditedEntity {

    //用户名
    private String userId;

    //用户名
    @TableField(exist = false)
    private String userName;

    //用户操作
    private String operation;
    //请求方法
    private String method;
    //请求参数
    private String params;

    private String result;

    private String errorMessage;

    private String errorStackTrace;

    private LocalDateTime executeTime;
    //执行时长(毫秒)
    private Long duration;
    //IP地址
    private String ip;

}
