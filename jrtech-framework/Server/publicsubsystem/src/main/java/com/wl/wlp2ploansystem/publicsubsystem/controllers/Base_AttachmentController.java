package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.Base_AttachmentGetListInputDto;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Attachment;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategory;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategoryClassification;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_AttachmentService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCategoryTreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCreateCategoryOfClassificationListInput;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentClassificationIdsWithBusinessDocIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_Attachment", produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_AttachmentController {

    @Autowired
    private Base_AttachmentService service;

    @PostMapping("/getTreeCategoryClassifications")
    @Log("读取附件分类列表（树形）")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public Collection<TreeObject<Base_AttachmentCategoryClassification>> getTreeCategoryClassifications() {
        return service.getTreeCategoryClassifications();
    }

    @PostMapping(value = "/getCategoryClassification")
    @Log("读取附件分类信息")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public Base_AttachmentCategoryClassification getCategoryClassification(String id) {
        return service.getCategoryClassification(id);
    }

    @PostMapping(value = "/saveCategoryClassification")
    @Log("新增/修改附件分类")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public HashMap<String, String> saveCategoryClassification(@RequestBody Base_AttachmentCategoryClassification input) {
        input.setVisible(true);
        String id = service.saveCategoryClassification(input);

        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }

    @RequestMapping(value = "/createCategoryOfClassificationList", method = RequestMethod.POST)
    @Log("建立附件分类、类别关系信息")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public void createCategoryOfClassificationList(@RequestBody Base_AttachmentCreateCategoryOfClassificationListInput input) {
        service.createCategoryOfClassificationList(input);
    }

    @RequestMapping(value = "/deleteCategoryClassification", method = RequestMethod.POST)
    @Log("删除附件分类信息")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public void deleteCategoryClassification(String id) {
        service.deleteCategoryClassification(id);
    }

    @RequestMapping(value = "/getCategoryiesByClassificationId", method = RequestMethod.POST)
    @Log("读取附件分类下的附件类别列表")
    @PreAuthorize("hasAuthority('common')")
    public Collection<Base_AttachmentCategory> getCategoryiesByClassificationId(@RequestBody CommonCategoryIdAndSortingDto input) {
        Collection<Base_AttachmentCategory> result = service.getCategoryiesByClassificationId(input.getCategoryId()
                , input.getSortingFiled()
                , input.getSortDirection());

        return result;
    }

    @RequestMapping(value = "/getCategoriesTreeWithClassificationId", method = RequestMethod.POST)
    @Log("读取附件类别列表(树形)")
    @PreAuthorize("hasAuthority('common')")
    public Collection<TreeObject<Base_AttachmentCategoryTreeObject>> getCategoriesTreeWithClassificationId(@RequestBody Base_AttachmentClassificationIdsWithBusinessDocIdDto inputDto) {
        return service.getCategoriesTreeWithClassificationId(inputDto);
    }
    @RequestMapping(value = "/getAllCategories", method = RequestMethod.POST)
    @Log("读取所有附件类别列表")
    @PreAuthorize("hasAuthority('common')")
    public Collection<Base_AttachmentCategory> getAllCategories() {
        return service.getAllCategories();
    }

    @RequestMapping(value = "/getFilterCategories", method = RequestMethod.POST)
    @Log("筛选附件类别列表")
    @PreAuthorize("hasAuthority('common')")
    public Collection<Base_AttachmentCategory> getFilterCategories(CommonSearchInputDto input) {

        EntityWrapper<Base_AttachmentCategory> ew = new EntityWrapper<Base_AttachmentCategory>();


        if (input.getFilters() != null && input.getFilters().size() > 0) {
            input.getFilters().forEach((k, v) -> {
                String field = k;
                List<String> inValues = v;
                ew.in(field, v);

            });
        }
        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("name", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        return service.getFilterCategories(ew);
    }

    @RequestMapping(value = "/getCategory", method = RequestMethod.POST)
    @Log("读取附件类别信息")
    public Base_AttachmentCategory getCategory(String id) {
        return service.getCategory(id);
    }

    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    @Log("新增/修改附件类别信息")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public HashMap<String, String> saveCategory(@RequestBody Base_AttachmentCategory input) {
        input.setVisible(true);
        String id = service.saveCategory(input);

        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }

    @RequestMapping(value = "/batchDeleteCategories", method = RequestMethod.POST)
    @Log("批量删除附件类别信息")
    @PreAuthorize("hasAuthority('menu_base_attachmentcategory')")
    public void batchDeleteCategories(@RequestBody List<String> ids) {
        service.batchDeleteCategories(ids);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @Log("读取附件列表")
    public Collection<Base_Attachment> getList(@RequestBody Base_AttachmentGetListInputDto input) {
        EntityWrapper<Base_Attachment> ew = new EntityWrapper<Base_Attachment>();
        ew.eq("attachmentCategoryId",input.getCategoryId());
        ew.eq("businessDocId",input.getBusinessDocId());

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }

        return service.getList(ew);
    }
    @RequestMapping(value = "/getListByClassificationIds", method = RequestMethod.POST)
    @Log("预览附件列表")
    public Collection<Base_Attachment> getListByClassificationIds(@RequestBody Base_AttachmentClassificationIdsWithBusinessDocIdDto input) {
        return service.getListByClassificationIds(input);
    }
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @Log("读取附件信息")
    public Base_Attachment get(String id) {
        return service.get(id);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Log("新增附件信息")
    public HashMap<String, String> save(@RequestBody Base_Attachment input) {
        String id = service.save(input);

        HashMap<String, String> result = new HashMap<String, String>();
        result.put("id", id);

        return result;
    }

    @RequestMapping(value = "/saveList", method = RequestMethod.POST)
    @Log("批量新增附件信息")
    public void saveList(@RequestBody List<Base_Attachment> inputList) {
        service.saveList(inputList);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @Log("删除附件信息")
    public void delete(String id) {
        service.delete(id);
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @Log("批量删除附件信息")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}
