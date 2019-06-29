package com.wl.wlp2ploansystem.loansubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelationFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.util.List;

public interface PL_LoanEnterCustomerRelationService extends BaseService {

    PL_LoanEnterCustomerRelation get(String id);

    String save(PL_LoanEnterCustomerRelation input);

    void delete(String id);

    void batchDelete(List<String> ids);

    Page<PL_LoanEnterCustomerRelation> getPagedList(Page<PL_LoanEnterCustomerRelation> pager, EntityWrapper<PL_LoanEnterCustomerRelation> ew);


    List<PL_LoanEnterCustomerRelationFull> getList(Wrapper<PL_LoanEnterCustomerRelationFull> ew);

    PL_LoanEnterCustomerRelationFull getFull(String id);
}

