package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionaryCategory;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_DataDictionary",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_DataDictionaryController {
    @Autowired
    private Base_DataDictionaryService service;

    @RequestMapping(value = "/getTreeDataDictionaryCategories", method = RequestMethod.POST)
    @Log(disabled = true)
    public Collection<TreeObject<Base_DataDictionaryCategory>> getTreeDataDictionaryCategories(){
        return service.getTreeDataDictionaryCategories();
    }

    /***
     * 更新父节点和序号
     * @param inputDto 输入Dto
     */
    @PostMapping("/updateCategoryParentIdAndSortIndex")
    @Log("更新父节点及序号")
    public void updateCategoryParentIdAndSortIndex(@Valid @RequestBody TreeNodeDropInputDto inputDto){
        service.updateCategoryParentIdAndSortIndex(inputDto);
    }
    @RequestMapping(value = "/getDataDictionaries", method = RequestMethod.POST)
    @Log("读取字典项列表")
    public Collection<Base_DataDictionary> getDataDictionaries(@RequestBody CommonCategoryIdAndSortingDto input){

        EntityWrapper<Base_DataDictionary> ew = new EntityWrapper<Base_DataDictionary>();
        ew.eq("dataDictionaryCategoryId",input.getCategoryId());

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        return service.getDataDictionaries(ew);
    }
    @RequestMapping(value = "/getDataDictionay", method = RequestMethod.POST)
    @Log("读取字典项信息")
    public Base_DataDictionary getDataDictionay(String id){
      Base_DataDictionary dataDictionary= service.getDataDictionay(id);
      return dataDictionary;
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Log("新增/修改字典项")
    @PreAuthorize("hasAuthority('menu_base_datadictionary')")
    public HashMap<String,String> save(@RequestBody  Base_DataDictionary input){
        String id= service.save(input);

        HashMap<String,String> result = new HashMap<String,String>();
        result.put("id",id);

        return result;
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @Log("批量删除字典项")
    @PreAuthorize("hasAuthority('menu_base_datadictionary')")
    public void batchDelete(@RequestBody  List<String> ids){
         service.batchDelete(ids);
    }
}
