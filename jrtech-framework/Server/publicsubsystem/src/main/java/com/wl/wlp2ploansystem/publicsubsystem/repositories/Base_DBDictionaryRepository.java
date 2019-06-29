package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DBDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//https://www.cnblogs.com/yuan951/p/7338871.html
public interface Base_DBDictionaryRepository {
    List<Base_DBDictionary> queryDBDictionary(@Param("value")String value);
}
