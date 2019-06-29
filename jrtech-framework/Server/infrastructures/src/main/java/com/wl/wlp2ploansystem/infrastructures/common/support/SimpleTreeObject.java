package com.wl.wlp2ploansystem.infrastructures.common.support;

import lombok.Data;

/**
 * 简单树结构实体
 */
@Data
public class SimpleTreeObject implements ITreeObject { 
	private String id;
	private String parentId;
	private String name;
	private String nodeType;

	public SimpleTreeObject(){

	}

	public SimpleTreeObject(String id, String parentId, String name, String nodeType) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.nodeType = nodeType;
	}
	
	
}
