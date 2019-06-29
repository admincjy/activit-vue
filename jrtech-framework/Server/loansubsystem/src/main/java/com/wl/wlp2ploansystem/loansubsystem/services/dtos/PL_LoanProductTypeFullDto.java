package com.wl.wlp2ploansystem.loansubsystem.services.dtos;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductSubType;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductType;
import lombok.Data;

import java.util.List;

@Data
public class PL_LoanProductTypeFullDto {
    private PL_LoanProductType headerEntity;
    private List<PL_LoanProductSubType> detailEntityList;
    private List<String> deletedDetailIdList;
}
