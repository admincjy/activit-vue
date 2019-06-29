package com.wl.wlp2ploansystem.infrastructures.common.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 乐观锁并发冲突异常类
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OptimisticConcurrencyException extends RuntimeException {

    public final static int  STATUS_CODE = 8888;
    private Integer statusCode = 8888;
    private String message = "数据冲突，请刷新后重试！";

    public OptimisticConcurrencyException(){

    }
    public OptimisticConcurrencyException(String message) {
        this.message = message;
    }


}
