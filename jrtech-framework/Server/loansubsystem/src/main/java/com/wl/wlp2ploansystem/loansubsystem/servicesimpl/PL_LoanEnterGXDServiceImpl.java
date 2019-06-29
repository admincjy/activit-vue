package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.loansubsystem.entities.*;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanDocRepository;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanEnterAssetInformationRepository;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanEnterCustomerRelationRepository;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanEnterGXDRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterGXDService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanProductTypeService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanEnterGXDSaveInDto;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SequenceNumberService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("PL_LoanEnterGXDService")
public class PL_LoanEnterGXDServiceImpl extends BaseServiceImpl implements PL_LoanEnterGXDService {

    @Autowired
    private PL_LoanEnterGXDRepository repository;
    @Autowired
    private PL_LoanDocRepository baseDocRepository;

    @Autowired
    private PL_LoanProductTypeService loanProductTypeService;

    @Autowired
    private Base_SequenceNumberService sequenceNumberService;

    @Autowired
    private PL_LoanEnterAssetInformationRepository assetInformationRepository;

    @Autowired
    private PL_LoanEnterCustomerRelationRepository customerRelationRepository;


    @Override
    @Transactional(readOnly = true)
    public PL_LoanEnterGXD get(String id) {
        return  repository.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PL_LoanEnterGXDFull getFull(String id) {
        return  repository.getFull(id);
    }

    @Override
    @Transactional
    public String save(PL_LoanEnterGXDSaveInDto input) {
        PL_LoanEnterGXD headerEntity = input.getHeaderEntity();

        List<PL_LoanEnterAssetInformation> assetInformationList = input.getAssetInformationList();
        List<String> deletedAssetInformationIdList = input.getDeletedAssetInformationIdList();

        PL_LoanProductSubType loanProductSubTypeEntity = loanProductTypeService.getLoanProductSubType(headerEntity.getLoanProductSubTypeId());
        PL_LoanDoc basicEntity = null;
        boolean toInserted = false;
        if (StringUtils.isEmpty(headerEntity.getCustomerCode())){
            basicEntity = new PL_LoanDoc();
            basicEntity.setId(headerEntity.getId());

            headerEntity.setCustomerCode(sequenceNumberService.getSequenceNumber("pl_LoanApplyDoc_Code"));
            basicEntity.setCustomerCode(headerEntity.getCustomerCode());

            toInserted = true;

        }else{
            toInserted = false;
            basicEntity = baseDocRepository.selectById(headerEntity.getId());
        }

        basicEntity.setCustomerName(headerEntity.getCustomerName());
        basicEntity.setCustomerCardNO(headerEntity.getCustomerCardNO());
        basicEntity.setCustomerMobile(headerEntity.getMobile());
        basicEntity.setLoanProductSubTypeId(headerEntity.getLoanProductSubTypeId());
        basicEntity.setTrackingPersonInfoMRId(headerEntity.getTrackingPersonInfoMRId());
        basicEntity.setOriginalLoanMoneyAmount(headerEntity.getLoanMoneyAmount());
        basicEntity.setOriginalLoanTermCount(headerEntity.getLoanTermCount());
        basicEntity.setLoanMoneyAmount(headerEntity.getLoanMoneyAmount());
        basicEntity.setLoanTermCount(headerEntity.getLoanTermCount());
        basicEntity.setLoanApplyDate(headerEntity.getLoanApplyDate());
        basicEntity.setCustomerName(headerEntity.getCustomerName());
        basicEntity.setFormTemplateTypeId(loanProductSubTypeEntity.getFormTemplateTypeId());

        basicEntity.setReturnMoneyMethodId(headerEntity.getReturnMoneyMethodId());
        basicEntity.setLoanRegPurpose(headerEntity.getLoanRegPurpose());
        basicEntity.setReturnMoneySource(headerEntity.getReturnMoneySource());
        basicEntity.setFundsSourceId(headerEntity.getFundsSourceId());
        basicEntity.setLoanRegPurpose(headerEntity.getLoanRegPurpose());
        basicEntity.setBankOfOpen(headerEntity.getBankOfOpen());
        basicEntity.setBankAccounName(headerEntity.getBankAccounName());
        basicEntity.setBankAccountNo(headerEntity.getBankAccountNo());

        if (toInserted){
            baseDocRepository.insertAllColumn(basicEntity);
            repository.insert(headerEntity);
        } else {

            baseDocRepository.updateAllColumnById(basicEntity);
            repository.updateAllColumnById(headerEntity);
        }

        if(assetInformationList !=null){
            assetInformationList.forEach(p->{
                if(StringUtils.isEmpty(p.getId())){
                    p.setId(IdGenerator.get());
                    p.setLoanDocId(headerEntity.getId());
                    assetInformationRepository.insert(p);
                }else {
                    assetInformationRepository.updateAllColumnById(p);
                }
            });
        }

        if(deletedAssetInformationIdList != null && deletedAssetInformationIdList.size()>0){
            assetInformationRepository.deleteBatchIds(deletedAssetInformationIdList);
        }

        return headerEntity.getId();
    }


    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }
}
