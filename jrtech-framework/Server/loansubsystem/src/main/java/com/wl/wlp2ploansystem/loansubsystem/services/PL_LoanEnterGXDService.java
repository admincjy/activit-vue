package com.wl.wlp2ploansystem.loansubsystem.services;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterGXD;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterGXDFull;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterGXDSaveInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

public interface PL_LoanEnterGXDService extends BaseService {

    PL_LoanEnterGXDFull getFull(String id);

    PL_LoanEnterGXD get(String id);


    String save(PL_LoanEnterGXDSaveInDto input);

    void delete(String id);

}
