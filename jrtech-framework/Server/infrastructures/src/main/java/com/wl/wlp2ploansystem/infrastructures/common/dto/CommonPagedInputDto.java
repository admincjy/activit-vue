package com.wl.wlp2ploansystem.infrastructures.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用分页查询数据传输对象
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonPagedInputDto extends CommonSearchInputDto {
	/**
	 * 查询记录行数
	 * */
	private Integer maxResultCount;

	/**
	 * 查询起始值
	 * */
	private Integer skipCount;

	@Override
	public boolean supports(Class clazz) {
		return CommonPagedInputDto.class.equals(clazz);
	}

}