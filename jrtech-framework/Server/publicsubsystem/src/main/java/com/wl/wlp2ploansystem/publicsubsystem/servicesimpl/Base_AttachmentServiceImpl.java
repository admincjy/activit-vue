package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.*;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_AttachmentCategoryClassificationRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_AttachmentCategoryOfClassificationRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_AttachmentCategoryRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_AttachmentRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_AttachmentService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCategoryTreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCreateCategoryOfClassificationListInput;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentClassificationIdsWithBusinessDocIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class Base_AttachmentServiceImpl  extends BaseServiceImpl implements Base_AttachmentService {

    @Autowired
    private Base_AttachmentCategoryClassificationRepository categoryClassificationRepository;

    @Autowired
    private Base_AttachmentCategoryRepository categoryRepository;

    @Autowired
    private Base_AttachmentCategoryOfClassificationRepository categoryOfClassificationRepository;

    @Autowired
    private Base_AttachmentRepository repository;

    @Override
    @Cacheable("Base_Attachment_getAllCategoryClassifications")
    public Collection<Base_AttachmentCategoryClassification> getAllCategoryClassifications() {
        return categoryClassificationRepository.selectList(null);
    }


    @Override
    public Collection<TreeObject<Base_AttachmentCategoryClassification>> getTreeCategoryClassifications(){
        Collection<Base_AttachmentCategoryClassification> categoryClassificationEntityList = this.getAllCategoryClassifications()
                .stream().filter(p->p.getVisible() != null && p.getVisible() == true)
                .sorted( Comparator.comparing(Base_AttachmentCategoryClassification::getName))
                .collect(Collectors.toList());

        TreeService<Base_AttachmentCategoryClassification> treeService = new TreeService<Base_AttachmentCategoryClassification>(categoryClassificationEntityList);
        Collection<TreeObject<Base_AttachmentCategoryClassification>> treeCategories = treeService.getTreeRootList();

        return treeCategories;
    }
    @Override
    public Base_AttachmentCategoryClassification getCategoryClassification(String id)
    {
        Base_AttachmentCategoryClassification item=  categoryClassificationRepository.selectById(id);
        return  item;
    }
    @Transactional
    @Override
    public String saveCategoryClassification(Base_AttachmentCategoryClassification input) {

        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            categoryClassificationRepository.insert(input);
        } else {
            categoryClassificationRepository.updateAllColumnById(input);
        }

        return input.getId();
    }

    @Transactional
    @Override
    public void deleteCategoryClassification(String id) {
        Base_AttachmentCategoryClassification categoryClassification = this.getCategoryClassification(id);
        if(categoryClassification == null){
            return;
        }
        categoryClassificationRepository.deleteById(id);

        EntityWrapper<Base_AttachmentCategoryOfClassification> attachmentCategoryOfClassificationEntityWrapper =new
                EntityWrapper<>();
        attachmentCategoryOfClassificationEntityWrapper.eq("categoryClassificationId",id);
        categoryOfClassificationRepository.delete(attachmentCategoryOfClassificationEntityWrapper);

    }
    @Override
    @Transactional
    public void createCategoryOfClassificationList(Base_AttachmentCreateCategoryOfClassificationListInput input) {

        categoryOfClassificationRepository.deleteByCategoryClassificationId(input.getCategoryClassificationId());
        Collection<Base_AttachmentCategoryOfClassification> list = input.getCategoryOfClassificationList();
        if(list !=null) {
            list.forEach(p -> {
                p.setCategoryClassificationId(input.getCategoryClassificationId());
                categoryOfClassificationRepository.save(p.getCategoryId()
                        , p.getCategoryClassificationId()
                        , p.getRequired());
            });
        }

    }
    @Override
    public Collection<Base_AttachmentCategory> getAllCategories() {

        return categoryRepository.getAllCategoryies();
    }
    @Override
    public Collection<Base_AttachmentCategory> getFilterCategories(EntityWrapper<Base_AttachmentCategory> ew) {
        return categoryRepository.selectList(ew);
    }
    @Override
    public Collection<TreeObject<Base_AttachmentCategoryTreeObject>> getCategoriesTreeWithClassificationId(Base_AttachmentClassificationIdsWithBusinessDocIdDto inputDto) {
        List<String> cagegoryClassificationIds = inputDto.getCagegoryClassificationIds();
        String businessDocId = inputDto.getBusinessDocId();

        List<Base_AttachmentCategoryTreeObject> treeList = new ArrayList<Base_AttachmentCategoryTreeObject>();

        Collection<Base_AttachmentCategory> categorylist = this.getAllCategories().stream().filter(p->cagegoryClassificationIds.contains(p.getCategoryClassificationId()))
                .sorted( Comparator.comparing(Base_AttachmentCategory::getSortIndex))
                .collect(Collectors.toList());

        final  List<keyIntegerValuePair> categoryRowsCountR = getAttachmnetCategoryRowsCount(businessDocId,categorylist.stream().map(p -> p.getId()).collect(Collectors.toList()));

        categorylist.forEach(p -> {
            String name = p.getName();
            Integer categoryAttachmentCount = null;
            if(categoryRowsCountR != null ) {
                keyIntegerValuePair nameItemCount = categoryRowsCountR.stream().filter(sp->sp.getKey().equals(p.getId())).findFirst().orElse(null);
                if(nameItemCount !=null){
                    categoryAttachmentCount = nameItemCount.getValue();
                }
            }

            Base_AttachmentCategoryTreeObject be = new Base_AttachmentCategoryTreeObject(p.getId(), p.getCategoryClassificationId(), name, false,false,categoryAttachmentCount,"AttachmentCategory");
                be.setRequired(p.getRequired());
                be.setChecked(true);

                treeList.add(be);
        });
        Collection<Base_AttachmentCategoryClassification> categoryClassificationlist =  this.getAllCategoryClassifications()
                .stream().filter(p->cagegoryClassificationIds.contains(p.getId()))
                .sorted( Comparator.comparing(Base_AttachmentCategoryClassification::getId))
                .collect(Collectors.toList());

        categoryClassificationlist.forEach(p -> {
            Base_AttachmentCategoryTreeObject be = new Base_AttachmentCategoryTreeObject(p.getId(), p.getParentId(), p.getName(),false,false,null, p.getNodeType());
                be.setChecked(true);
                treeList.add(be);
        });

        TreeService<Base_AttachmentCategoryTreeObject> treeFactory = new TreeService<Base_AttachmentCategoryTreeObject>(treeList);

        Collection<TreeObject<Base_AttachmentCategoryTreeObject>> results = treeFactory.getTreeRootList();

        return results;

    }
    @Override
    public Collection<Base_AttachmentCategory> getCategoryiesByClassificationId(String cagegoryClassificationId, String sortFiled, SortDirection sortDirection) {

        if (StringUtils.isEmpty(cagegoryClassificationId))
        {
            return  Collections.emptyList();
        }
        List<Base_AttachmentCategory> categories = this.getAllCategories()
                .stream()
                .filter(p-> cagegoryClassificationId.equalsIgnoreCase(p.getCategoryClassificationId()))
                .sorted( Comparator.comparing(Base_AttachmentCategory::getSortIndex))
                .collect(Collectors.toList());


        PropertyComparator.sort(categories,
                new MutableSortDefinition(sortFiled, true, sortDirection == SortDirection.Asc));
        return Collections.unmodifiableList(categories);
    }

    @Override
    public Base_AttachmentCategory getCategory(String id)
    {
      return categoryRepository.selectById(id);
    }
    @Override
    @Transactional
    public String saveCategory(Base_AttachmentCategory input) {

        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            categoryRepository.insert(input);
        } else {
            Integer effectRecords=  categoryRepository.updateAllColumnById(input);

            if(effectRecords == 0){
                throw new OptimisticConcurrencyException();
            }
        }
        categoryOfClassificationRepository.save(input.getId()
                ,input.getCategoryClassificationId()
                ,input.getRequired());


        return input.getId();
    }


    @Override
    @Transactional
    public void batchDeleteCategories(List<String> ids) {

        categoryRepository.deleteBatchIds(ids);

        EntityWrapper<Base_AttachmentCategoryOfClassification>  categoryOfClassificationEntityWrapper = new EntityWrapper<>();
        categoryOfClassificationEntityWrapper.in("categoryId",ids);
        categoryOfClassificationRepository.delete(categoryOfClassificationEntityWrapper);

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Base_Attachment> getList(EntityWrapper<Base_Attachment> ew) {
        return repository.selectList(ew);

    }
    @Override
    @Transactional(readOnly = true)
    public Collection<Base_Attachment> getListByClassificationIds(Base_AttachmentClassificationIdsWithBusinessDocIdDto inputDto) {

        List<String> cagegoryClassificationIds = inputDto.getCagegoryClassificationIds();
        String businessDocId = inputDto.getBusinessDocId();


        Collection<String> categoryIdlist = this.getAllCategories().stream().filter(p->cagegoryClassificationIds.contains(p.getCategoryClassificationId()))
                .sorted( Comparator.comparing(Base_AttachmentCategory::getId))
                .map(p->p.getId())
                .collect(Collectors.toList());


        EntityWrapper<Base_Attachment>  attachmentEntityWrapper = new EntityWrapper<>();
        attachmentEntityWrapper.in("attachmentCategoryId",categoryIdlist);
        attachmentEntityWrapper.eq("businessDocId",businessDocId);
        attachmentEntityWrapper.orderBy("attachmentCategoryId", true)
                .orderBy("id", false);

        return repository.selectList(attachmentEntityWrapper);

    }


    /* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#get(java.lang.String)
	 */

    @Override
    public Base_Attachment get(String id) {
        return repository.selectById(id);
    }

    /* (non-Javadoc)
     * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#save(com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization)
     */

    @Override
    @Transactional
    public String saveSingle(Base_Attachment input) {

        EntityWrapper<Base_Attachment> attachmentEW=new EntityWrapper<Base_Attachment>();
        attachmentEW.eq("businessDocId",input.getBusinessDocId());
        attachmentEW.eq("attachmentCategoryId",input.getAttachmentCategoryId());
        repository.delete(attachmentEW);

       return this.save(input);
    }
    @Override
    @Transactional
    public String save(Base_Attachment input) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
        }
        repository.insert(input);

        return input.getId();
    }
    @Override
    @Transactional
    public void saveList(List<Base_Attachment> inputList)
    {
        inputList.forEach(p->{
            save(p);
        });

    }
    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        ids.forEach(p -> delete(p));
    }

    /***
     * 记录下载次数
     * @param id 附件Id
     */
    @Override
    public void recordDownloadFileCount(String id) {
        Base_Attachment entity = repository.selectById(id);
        Integer oldDownloadCount =entity.getDownloadCount();
        if(oldDownloadCount == null){
            oldDownloadCount = 0;
        }

        entity.setDownloadCount(oldDownloadCount ++ );
        Integer effectRecords= repository.updateAllColumnById(entity);

        if(effectRecords == 0){
            throw new OptimisticConcurrencyException();
        }
    }
    @Override
    public Boolean IsCategoriesAllValid(String businessDocId, List<String> categoryOfClassificationIdList)
    {
        EntityWrapper<Base_AttachmentCategoryOfClassification> categoryOfClassificationEW=new EntityWrapper<>();
        categoryOfClassificationEW.in("categoryClassificationId",categoryOfClassificationIdList);
        Collection<Base_AttachmentCategoryOfClassification> categoryOfClassificationList = categoryOfClassificationRepository.selectList(categoryOfClassificationEW);

     List<String> categoryIdList = categoryOfClassificationList.stream()
             .filter(p->p.getRequired())
             .map(p->p.getCategoryId()).distinct().collect(Collectors.toList());

     if (categoryIdList.size() == 0){
         return true;
     }
        EntityWrapper<Base_Attachment> attachmentEW=new EntityWrapper<Base_Attachment>();
        attachmentEW.eq("businessDocId",businessDocId);
        attachmentEW.in("attachmentCategoryId",categoryIdList);

        List<keyIntegerValuePair> categoryRowsCountMap = repository.getCategoryRowsCount(attachmentEW);

        return categoryRowsCountMap.size() == categoryIdList.size();

    }
    private List<keyIntegerValuePair>  getAttachmnetCategoryRowsCount(String businessDocId,List<String> categoryIdList) {

        if(categoryIdList == null || categoryIdList.size() == 0){
            return  null;
        }
        EntityWrapper<Base_Attachment> categoryRowsCountEW = new EntityWrapper<>();
        categoryRowsCountEW.in("attachmentCategoryId", categoryIdList);
        categoryRowsCountEW.eq("businessDocId",businessDocId);

        return repository.getCategoryRowsCount(categoryRowsCountEW);

    }

}
