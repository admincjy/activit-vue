package com.wl.wlp2ploansystem.infrastructures.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 类别查询排序数据传输对象
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCategoryIdAndSortingDto extends CommonCategoryIdDto implements Validator {

	/**
	 * 排序字段
	 */
	private String sorting;

	/**
	 * 前端筛选列表
	 */
	private HashMap<String,ArrayList<String>> filters;

	public HashMap<String, ArrayList<String>> getFilters() {
		return filters;
	}

	public void setFilters(HashMap<String, ArrayList<String>> filters) {
		this.filters = filters;
	}


	public boolean supports(Class clazz) {
		return CommonCategoryIdAndSortingDto.class.equals(clazz);
	}

	public SortDirection getSortDirection() {
		 return  SortDirections.getSortDirection(this.sorting);
	}

	public String getSortingFiled() {
		return  SortDirections.getSortingFiled(this.sorting);
	}
	public void validate(Object obj, Errors e) {
		//排序字段输入验证
		String sortingValideResult = SortDirections.validate(this.sorting);

		if (!StringUtils.isEmpty(sortingValideResult)) {
				e.rejectValue("sorting", sortingValideResult);
		}
	}
}
