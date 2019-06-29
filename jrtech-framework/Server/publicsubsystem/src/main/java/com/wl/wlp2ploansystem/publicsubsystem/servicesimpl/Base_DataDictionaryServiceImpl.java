package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedType;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionaryCategory;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_DataDictionaryCategoryRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_DataDictionaryRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Base_DataDictionaryServiceImpl extends BaseServiceImpl implements Base_DataDictionaryService {

    @Autowired
    private Base_DataDictionaryRepository repository;

    @Autowired
    private Base_DataDictionaryCategoryRepository categoryRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Collection<Base_DataDictionary> getAllDataDictionaries() {
        List<Base_DataDictionary> dbList = repository.selectList(null);
        return  dbList;
    }
    @Override
    public Collection<Base_DataDictionaryCategory> getAllDataDictionaryCategories(){
        List<Base_DataDictionaryCategory> dbList = categoryRepository.selectList(null);
        return  dbList;
    }
    @Override
    public Collection<TreeObject<Base_DataDictionaryCategory>> getTreeDataDictionaryCategories(){

        Collection<Base_DataDictionaryCategory> categoryEntityList = this.getAllDataDictionaryCategories()
                .stream().filter(p->p.getVisible() != null && p.getVisible() == true)
                .collect(Collectors.toList());

        TreeService<Base_DataDictionaryCategory> treeService = new TreeService<Base_DataDictionaryCategory>(categoryEntityList);
        Collection<TreeObject<Base_DataDictionaryCategory>> treeCategories = treeService.getTreeRootList();

        return treeCategories;
    }
    /***
     * 更新父节点和序号
     * @param inputDto 输入Dto
     */
    @Override
    @Transactional
    public void updateCategoryParentIdAndSortIndex(TreeNodeDropInputDto inputDto){

        Base_DataDictionaryCategory  entity = categoryRepository.selectById(inputDto.getId());
        entity.setParentId(inputDto.getNewParentId());
        //ntity.setSortIndex(inputDto.getNewSortIndex());

        categoryRepository.updateAllColumnById(entity);

        applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

        applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
    }

    @Override
    public Collection<Base_DataDictionary> getDataDictionaries(EntityWrapper<Base_DataDictionary> entityWrapper) {

        List<Base_DataDictionary> dbList = repository.selectList(entityWrapper);
        return  dbList;
    }

    @Override
    public Base_DataDictionary getDataDictionay(String id) {
        return repository.selectById(id);
    }
    @Override
    @Transactional
    public String save(Base_DataDictionary input) {

        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            repository.insert(input);
        } else {
            Integer effectRecords= repository.updateAllColumnById(input);

            if(effectRecords == 0){
                throw new OptimisticConcurrencyException();
            }
        }

        applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

        applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));

        return input.getId();
    }


    @Override
    @Transactional
    public void batchDelete(List<String> ids) {

        repository.deleteBatchIds(ids);

        applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

        applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
    }


}
