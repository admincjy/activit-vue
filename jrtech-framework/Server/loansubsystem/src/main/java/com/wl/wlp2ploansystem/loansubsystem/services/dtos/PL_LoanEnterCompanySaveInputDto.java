package com.wl.wlp2ploansystem.loansubsystem.services.dtos;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCompany;
import lombok.Data;

import java.util.List;

@Data
public class PL_LoanEnterCompanySaveInputDto {
    private PL_LoanEnterCompany headerEntity;

    private List<PL_LoanEnterAssetInformation> assetInformationList;
    private List<String> deletedAssetInformationIdList;

}
