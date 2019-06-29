package com.wl.wlp2ploansystem.infrastructures.common.support;

import com.baomidou.mybatisplus.toolkit.IdWorker;

/**
 * 唯一编码生成器
 */
public class IdGenerator {
    /**
     * 生成唯一编码
     */
    public  static  String get(){
        return String.valueOf(IdWorker.getId());
    }
}
