package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DBDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_DBDictionaryRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_DBDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Service
public class Base_DBDictionaryServiceImpl extends BaseServiceImpl implements Base_DBDictionaryService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private Base_DBDictionaryRepository repository;

    @Override
    public List<Base_DBDictionary> queryDBDictionary() throws SQLException{
        String dbName = dataSource.getConnection().getCatalog();
        return  repository.queryDBDictionary(dbName);
    }
}
