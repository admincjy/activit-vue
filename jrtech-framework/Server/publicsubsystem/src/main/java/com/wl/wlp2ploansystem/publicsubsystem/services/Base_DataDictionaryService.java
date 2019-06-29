package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionaryCategory;

import java.util.Collection;
import java.util.List;

public interface Base_DataDictionaryService {
    Collection<Base_DataDictionary> getAllDataDictionaries();

    Collection<Base_DataDictionaryCategory> getAllDataDictionaryCategories();

    Collection<TreeObject<Base_DataDictionaryCategory>> getTreeDataDictionaryCategories();
    void updateCategoryParentIdAndSortIndex(TreeNodeDropInputDto inputDto);

    Collection<Base_DataDictionary> getDataDictionaries(EntityWrapper<Base_DataDictionary> entityWrapper) ;

    Base_DataDictionary getDataDictionay(String id);

    String save(Base_DataDictionary input);

    void batchDelete(List<String> ids);
}
