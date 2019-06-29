package com.wl.wlp2ploansystem.infrastructures.common.dto;


import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 分页查询返回结果数据传输对象
 * */
@EqualsAndHashCode()
public class PagedResultOutput<T> {

	/**
	 * 共计
	 */
	private Integer totalCount;

	/**
	 * 当前页记录列表
	 */
	private List<T> items;

	/**
	 * 其它合计值
	 */
	private Map<String,Object> reduces;

	public PagedResultOutput( Integer totalCount,List<T> items){
		this.totalCount = totalCount;
		this.items=items;
	}

	public PagedResultOutput(Integer totalCount, List<T> items, Map<String, Object> reduces) {
		this.totalCount = totalCount;
		this.items = items;
		this.reduces = reduces;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getItems() {
		if(items == null){
			items = new ArrayList<T>();
		}
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public Map<String, Object> getReduces() {
		return reduces;
	}

	public void setReduces(Map<String, Object> reduces) {
		this.reduces = reduces;
	}


}
