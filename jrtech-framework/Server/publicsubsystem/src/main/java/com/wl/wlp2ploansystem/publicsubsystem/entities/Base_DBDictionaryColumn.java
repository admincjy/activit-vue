package com.wl.wlp2ploansystem.publicsubsystem.entities;

import lombok.Data;

@Data
public class Base_DBDictionaryColumn {

    private String ordinalPosition;   //序号
    private String columnName;        //字段名
    private String columnType;        //字段类型
    private String columnDefault;     //字段默认
    private String isNullable;        //可否空
    private String extra;             //其他
    private String columnKey;         //主键约束
    private String columnComment;     //字段注释
}
