package com.wl.wlp2ploansystem.loansubsystem.services;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCompany;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCompanyFull;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterCompanySaveInputDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

public interface PL_LoanEnterCompanyService extends BaseService {

    PL_LoanEnterCompanyFull getFull(String id);

    PL_LoanEnterCompany get(String id);

    String save(PL_LoanEnterCompanySaveInputDto input);


    void delete(String id);

}