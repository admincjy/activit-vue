package com.wl.wlp2ploansystem.infrastructures.common.support;

import java.math.BigDecimal;

/**
 * 货币 处理工具类
 */

public class MoneyHelper {
    public static BigDecimal format(BigDecimal value) {
        return format(value, 2);
    }

    /**
     * 货币format
     * @param value  货币
     * @param digits 保留小数位数
     * @return
     */
    private static BigDecimal format(BigDecimal value, int digits) {
       // return value.setScale(digits, BigDecimal.ROUND_HALF_UP); //四舍五入
       // return value.setScale(digits, BigDecimal.ROUND_CEILING);  //向上取整
        return value.setScale(digits, BigDecimal.ROUND_HALF_UP);  //向下取整
    }

    /**
     * 货币format
     * @param value  货币
     * @param digits 保留小数位数
     * @param roundingMode  舍入方式
     * @return
     */
    public static BigDecimal format(BigDecimal value, int digits,int roundingMode) {
        // return value.setScale(digits, BigDecimal.ROUND_HALF_UP); //四舍五入
        // return value.setScale(digits, BigDecimal.ROUND_CEILING);  //向上取整
        return value.setScale(digits, roundingMode);  //向下取整
    }

    /**
     * 向上取整
     */
    public static BigDecimal ceiling(BigDecimal value) {

        return  BigDecimal.valueOf(Math.ceil(value.doubleValue()));
    }
}
