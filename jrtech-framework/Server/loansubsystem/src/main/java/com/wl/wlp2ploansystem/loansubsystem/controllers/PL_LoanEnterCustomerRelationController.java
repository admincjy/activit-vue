package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdDto;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelationFull;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterCustomerRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/pl_loanentercustomerrelation",produces = {MediaType.APPLICATION_JSON_VALUE})

public class PL_LoanEnterCustomerRelationController {
    @Autowired
    private PL_LoanEnterCustomerRelationService service;

    @PostMapping("/getList")
    @Log("读取关联人信息列表")
    public List<PL_LoanEnterCustomerRelationFull> getList(@Valid @RequestBody CommonCategoryIdDto input) {
        EntityWrapper<PL_LoanEnterCustomerRelationFull> ew = new EntityWrapper<PL_LoanEnterCustomerRelationFull>();

        ew.eq("loanDocId",input.getCategoryId());
        ew.orderBy("id", false);

        return  service.getList(ew);

    }
    @PostMapping("/get")
    @Log("读取关联人信息")
    public PL_LoanEnterCustomerRelation get(String id) {
        return service.get(id);
    }
    @PostMapping("/save")
    @Log("新增/修改关联人信息")
    public HashMap<String,String> save(@RequestBody PL_LoanEnterCustomerRelation input) {
        String id =  service.save(input);

        HashMap<String,String> result = new HashMap<String,String>();
        result.put("id",id);

        return result;
    }
    @PostMapping("/delete")
    @Log("删除关联人信息")
    public void delete(String id) {
        service.delete(id);
    }
    @PostMapping("/batchDelete")
    @Log("批量删除关联人信息")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}