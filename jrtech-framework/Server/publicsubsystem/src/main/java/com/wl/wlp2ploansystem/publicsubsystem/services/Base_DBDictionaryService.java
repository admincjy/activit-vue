package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DBDictionary;

import java.sql.SQLException;
import java.util.List;

public interface Base_DBDictionaryService {

    List<Base_DBDictionary> queryDBDictionary() throws SQLException;
}
