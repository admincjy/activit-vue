package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Attachment;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategory;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategoryClassification;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCategoryTreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCreateCategoryOfClassificationListInput;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentClassificationIdsWithBusinessDocIdDto;

import java.util.Collection;
import java.util.List;

public interface Base_AttachmentService {

    void createCategoryOfClassificationList(Base_AttachmentCreateCategoryOfClassificationListInput input);

    Collection<Base_AttachmentCategoryClassification> getAllCategoryClassifications();

    Collection<TreeObject<Base_AttachmentCategoryClassification>> getTreeCategoryClassifications();

    Collection<Base_AttachmentCategory> getAllCategories();

    Collection<Base_AttachmentCategory> getFilterCategories(EntityWrapper<Base_AttachmentCategory> ew);

    Collection<TreeObject<Base_AttachmentCategoryTreeObject>> getCategoriesTreeWithClassificationId(Base_AttachmentClassificationIdsWithBusinessDocIdDto inputDto);
    Collection<Base_AttachmentCategory> getCategoryiesByClassificationId(String cagegoryClassificationId, String sortFiled, SortDirection sortDirection);

    Base_AttachmentCategoryClassification getCategoryClassification(String id);
    String saveCategoryClassification(Base_AttachmentCategoryClassification input);
    void deleteCategoryClassification(String id);

    Base_AttachmentCategory getCategory(String id);
    String saveCategory(Base_AttachmentCategory input);

    void batchDeleteCategories(List<String> ids);

    Collection<Base_Attachment> getListByClassificationIds(Base_AttachmentClassificationIdsWithBusinessDocIdDto inputDto);

    Collection<Base_Attachment> getList(EntityWrapper<Base_Attachment> ew);

    Base_Attachment get(String id);


    String save(Base_Attachment input);
    String saveSingle(Base_Attachment input) ;

    void saveList(List<Base_Attachment> inputList);


    void delete(String id);


    void batchDelete(List<String> ids);

    void recordDownloadFileCount(String id);
    Boolean IsCategoriesAllValid(String businessDocId, List<String> categoryOfClassificationIdList);
}
