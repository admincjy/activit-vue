package com.wl.wlp2ploansystem.loansubsystem.repositories;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCompany;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCompanyFull;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import org.apache.ibatis.annotations.Param;

public interface PL_LoanEnterCompanyRepository extends BaseRepository<PL_LoanEnterCompany> {
    PL_LoanEnterCompanyFull getFull(@Param("id")  String id);
}
