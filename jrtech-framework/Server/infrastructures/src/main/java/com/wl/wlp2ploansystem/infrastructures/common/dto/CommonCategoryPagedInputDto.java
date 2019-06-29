package com.wl.wlp2ploansystem.infrastructures.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类别分页查询数据传输对象
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCategoryPagedInputDto  extends CommonPagedInputDto {
    /**
     * 类别编码
     */
    private String categoryId;
}
