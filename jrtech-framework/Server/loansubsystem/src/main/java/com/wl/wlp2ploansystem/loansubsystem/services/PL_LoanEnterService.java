package com.wl.wlp2ploansystem.loansubsystem.services;

import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterSaveInDto;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterDeleteInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.io.IOException;
import java.util.Map;

public interface PL_LoanEnterService extends BaseService {
    Object get(String id);
    String  save(PL_LoanEnterSaveInDto jsonString) throws IOException;
    Map<String,String>  submit(PL_LoanEnterSaveInDto jsonString) throws IOException;
    void delete(PL_LoanEnterDeleteInDto input);

}
