package com.wl.wlp2ploansystem.infrastructures.common.eventbus;

/***
 * 实体变更类型
 */
public enum EntityChangedType {
    /***
     * 插入
     */
    insert,
    /***
     * 变更
     */
    update,

    /***
     * 插入或者变更
     */
    insertOrUpdate,
    /***
     * 删除
     */
    delete,
}
