package com.wl.wlp2ploansystem.infrastructures.common.dto;

import org.springframework.util.StringUtils;
/**
 * 排序工具类
 */
public class SortDirections {
    /**
     * 解决字符串中的排序方向
     */
    public static SortDirection getSortDirection(String sorting) {
        if (!StringUtils.isEmpty(sorting)) {
            String[] sortStringArray = sorting.split(" ");
            if (sortStringArray.length == 2) {
                if (sortStringArray[1].toLowerCase().startsWith("asc")) {
                    return SortDirection.Asc;
                }
                return SortDirection.Desc;
            }
        }
        return SortDirection.Asc;
    }
    /**
     * 解决字符串中的排序字段
     */
    public static String getSortingFiled(String sorting)  {
        if (!StringUtils.isEmpty(sorting)) {
            String[] sortStringArray = sorting.split(" ");
            if (sortStringArray.length == 2) {
                return sortStringArray[0];
            }
        }
        return null;
    }
    /**
     * 排序参数验证合法性
     */
    public static String validate(String sorting) {

        String sortingString = sorting == null ? null : sorting.trim().toLowerCase();

        if (!StringUtils.isEmpty(sortingString)) {
            if( (!sortingString.endsWith("desc")
            || !sortingString.endsWith("descending"))
                    &&
                    (!sortingString.endsWith("asc")
                    || !sortingString.endsWith("ascending"))) {
                return "排序参数无效！";
            }
            String[] sortStringArray = sortingString.split(" ");
            if (sortStringArray.length != 2) {
                return "排序参数无效！";
            }
        }
        return null;
    }
}