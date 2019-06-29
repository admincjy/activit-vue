package com.wl.wlp2ploansystem.publicsubsystem.controllers.clientJson;

import com.google.common.collect.Maps;
import com.wl.wlp2ploansystem.infrastructures.common.clientJson.ClientJsonManager;
import com.wl.wlp2ploansystem.infrastructures.common.jsscript.ScriptManager;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionaryCategory;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_DataDictionaryService;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Base_DataDictionaryClientJsonManager implements ClientJsonManager {
    public Base_DataDictionaryClientJsonManager(Base_DataDictionaryService service){
        this.service = service;
    }
    private Base_DataDictionaryService service;


    @Override
    public Map<String, Object> getClientJsonObject() {
        Collection<Base_DataDictionaryCategory> dataDictionaryCategories = service.getAllDataDictionaryCategories();
        Collection<Base_DataDictionary> dataDictionaries = service.getAllDataDictionaries();


        Map<String,Object> jsonObjects = new HashMap<>();

        for (Base_DataDictionaryCategory category : dataDictionaryCategories)
        {

            Collection<Base_DataDictionary> categoryItems = dataDictionaries.stream()
                    .filter(p->p.getDataDictionaryCategoryId().equals(category.getId()))
                    .sorted(Comparator.comparing(Base_DataDictionary::getSortIndex))
                    .collect(Collectors.toList());

            jsonObjects.put(category.getId().toLowerCase(), categoryItems);
        }

        return Collections.singletonMap("base_datadictionary",jsonObjects);
    }
}
