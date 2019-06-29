package com.wl.wlp2ploansystem.publicsubsystem.entities;

import lombok.Data;

import java.util.Collection;

@Data
public class Base_DBDictionary {

    private String tableName;         //表明
    private String tableComment;     //表注释

    private Collection<Base_DBDictionaryColumn> columns;

}
