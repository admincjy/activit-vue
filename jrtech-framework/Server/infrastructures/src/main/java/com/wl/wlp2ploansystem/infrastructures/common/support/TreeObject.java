package com.wl.wlp2ploansystem.infrastructures.common.support;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 树形实体
 */
public class TreeObject<E extends ITreeObject> {

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 父Id
	 */
	private String parentId;

	/**
	 * 父实体
	 */
	@JsonIgnore
	private  TreeObject<E> parent;

	/**
	 * 树层级
	 */
	private Integer level;

	/**
	 * 树的名称路径，根结点用“."表示，用";"做名称间的分隔符，如路径 .;name1;name2;
	 */
	private String path;

	private E self;

	private Collection<TreeObject<E>> items = new ArrayList<TreeObject<E>>();
	/**
	 * 树的Id路径
	 */
	private List<String> idPath = new ArrayList<>();

	/**
	 * 是否有下级结点
	 */
	public  boolean getHasChildren() {
		return  this.getItems().size()>0;
	}
	/**
	 * 树的父Id路径(不包含自身id)
	 */
	public  List<String> getParentIdPath() {
		List<String> parentIdPath = new ArrayList<>();
		if(idPath.size() == 0){
			return parentIdPath;
		}
		parentIdPath = CommonHelper.deepCopyList(idPath).subList(0,idPath.size()-1);

		return parentIdPath;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getId() {
		return id;

	}
	public String getName() {
		return name;
	}
	public void setSelf(E self) {
		this.self = self;
	}
	public E getSelf() {
		return self;
	}
	public Collection<TreeObject<E>> getItems() {
		return items;
	}

	public void setItems(Collection<TreeObject<E>> items) {
		this.items = items;
	}
	public List<String> getIdPath() {
		return idPath;
	}

	public void setIdPath(List<String> idPath) {
		this.idPath = idPath;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public TreeObject<E> getParent() {
		return parent;
	}

	public void setParent(TreeObject<E> parent) {
		this.parent = parent;
	}
}