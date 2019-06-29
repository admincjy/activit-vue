package com.wl.wlp2ploansystem.loansubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDoc;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.util.List;

public interface PL_LoanDocService extends BaseService {
    List<PL_LoanDoc> getList(EntityWrapper<PL_LoanDoc> ew);
    Page<PL_LoanDoc> getPagedList(Page<PL_LoanDoc> pager, EntityWrapper<PL_LoanDoc> ew);
    Page<PL_LoanDocFull> getFullPagedList(Page<PL_LoanDocFull> pager, EntityWrapper<PL_LoanDocFull> ew);
    PL_LoanDocFull getFull(String id);
    PL_LoanDoc get(String id);
    void save(PL_LoanDoc input);
    void delete(String id);

}
