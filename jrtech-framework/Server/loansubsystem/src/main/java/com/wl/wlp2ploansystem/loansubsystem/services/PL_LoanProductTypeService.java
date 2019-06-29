package com.wl.wlp2ploansystem.loansubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductSubType;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductType;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanProductTypeFullDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.util.List;

public interface PL_LoanProductTypeService extends BaseService {

    PL_LoanProductTypeFullDto get(String id);

    PL_LoanProductSubType getLoanProductSubType(String loanProductSubTypeId);

    String save(PL_LoanProductTypeFullDto input);

    void delete(String id);

    void batchDelete(List<String> ids);

    Page<PL_LoanProductType> getPagedList(Page<PL_LoanProductType> pager, EntityWrapper<PL_LoanProductType> ew);
    List<PL_LoanProductType> getLoanProductTypeList();
    List<PL_LoanProductSubType> getLoanProductSubTypeList();
}

