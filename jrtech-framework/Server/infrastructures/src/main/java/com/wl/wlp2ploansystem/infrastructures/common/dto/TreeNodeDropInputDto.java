package com.wl.wlp2ploansystem.infrastructures.common.dto;

import lombok.EqualsAndHashCode;

/***
 * 树结构拖动输入传输对象
 */
@EqualsAndHashCode()
public class TreeNodeDropInputDto {
    /***
     * id
     */
    private String id;
    /***
     * 新的父id
     */
    private String newParentId;
    /***
     * 新的序列号
     */
    private Integer newSortIndex;

    public String getId() {
        return id;
    }

    public TreeNodeDropInputDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getNewParentId() {
        return newParentId;
    }

    public TreeNodeDropInputDto setNewParentId(String newParentId) {
        this.newParentId = newParentId;
        return this;
    }

    public Integer getNewSortIndex() {
        return newSortIndex;
    }

    public TreeNodeDropInputDto setNewSortIndex(Integer newSortIndex) {
        this.newSortIndex = newSortIndex;
        return this;
    }
}
