package com.wl.wlp2ploansystem.infrastructures.common.aop;


/**
* 系统日志记录服务接口
* */
public interface LoggingService {
    /**
     * 日志记录
     * @param loggingDetails 系统日志记录内容
     */
    void save(LoggingDetails loggingDetails);
}
