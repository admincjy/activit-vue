package com.wl.wlp2ploansystem.infrastructures.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类别查询数据传输对象
 * */
@Data
@EqualsAndHashCode()
public class CommonCategoryIdDto {
	/**
	 * 类别编码
	 */
	private String categoryId;
}
