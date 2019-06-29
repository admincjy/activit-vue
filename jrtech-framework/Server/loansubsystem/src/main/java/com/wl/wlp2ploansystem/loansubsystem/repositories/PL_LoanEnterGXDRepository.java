package com.wl.wlp2ploansystem.loansubsystem.repositories;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterGXD;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterGXDFull;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import org.apache.ibatis.annotations.Param;

public interface PL_LoanEnterGXDRepository extends BaseRepository<PL_LoanEnterGXD> {
    PL_LoanEnterGXDFull getFull(@Param("id")  String id);
}