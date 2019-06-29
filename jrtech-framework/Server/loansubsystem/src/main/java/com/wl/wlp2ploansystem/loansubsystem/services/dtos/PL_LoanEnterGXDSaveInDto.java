package com.wl.wlp2ploansystem.loansubsystem.services.dtos;

import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterGXD;
import lombok.Data;

import java.util.List;

@Data
public class PL_LoanEnterGXDSaveInDto {

    private PL_LoanEnterGXD headerEntity;
    private List<PL_LoanEnterAssetInformation> assetInformationList;
    private List<String> deletedAssetInformationIdList;
}
