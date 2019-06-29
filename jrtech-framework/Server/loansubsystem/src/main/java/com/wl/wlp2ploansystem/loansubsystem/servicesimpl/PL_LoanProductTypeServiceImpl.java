package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductSubType;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductType;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanProductSubTypeRepository;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanProductTypeRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanProductTypeService;
import com.wl.wlp2ploansystem.loansubsystem.services.dtos.PL_LoanProductTypeFullDto;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PL_LoanProductTypeServiceImpl extends BaseServiceImpl implements PL_LoanProductTypeService {


    @Autowired
    private PL_LoanProductTypeRepository repository;
    @Autowired
    private PL_LoanProductSubTypeRepository subTypeRepository;

    @Transactional(readOnly = true)
    public Page<PL_LoanProductType> getPagedList(Page<PL_LoanProductType> page, EntityWrapper<PL_LoanProductType> ew) {

        if (null != ew) {
            ew.orderBy(page.getOrderByField(), page.isAsc());
        }
        return page.setRecords(repository.selectPage(page, ew));

    }
    @Override
    public    List<PL_LoanProductType> getLoanProductTypeList(){
        List<PL_LoanProductType> orglist = repository.selectList(null);
        return  orglist;
    }
    @Override
    public    List<PL_LoanProductSubType> getLoanProductSubTypeList(){
        List<PL_LoanProductSubType> rolelist = subTypeRepository.selectList(null);
        return  rolelist;
    }

    @Override
    @Transactional(readOnly = true)
    public PL_LoanProductTypeFullDto get(String id) {
        PL_LoanProductType entity = repository.selectById(id);
        EntityWrapper<PL_LoanProductSubType> detailEW = new EntityWrapper<PL_LoanProductSubType>();
        detailEW.eq("loanProductTypeId",id);
        detailEW.orderBy("id",true);
        List<PL_LoanProductSubType> detailList = subTypeRepository.selectList(detailEW);


        PL_LoanProductTypeFullDto dto = new PL_LoanProductTypeFullDto();
        dto.setHeaderEntity(entity);
        dto.setDetailEntityList(detailList);

        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    public PL_LoanProductSubType getLoanProductSubType(String loanProductSubTypeId){

        PL_LoanProductSubType  loanProductSubType = subTypeRepository.selectById(loanProductSubTypeId);

        return loanProductSubType;
    }

    @Override
    @Transactional
    public String save(PL_LoanProductTypeFullDto input) {

        PL_LoanProductType entity = input.getHeaderEntity();
        List<PL_LoanProductSubType> detailEntityList = input.getDetailEntityList();
        List<String> deletedDetailList = input.getDeletedDetailIdList();

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(IdGenerator.get());
            repository.insert(entity);
        } else {
            Integer effectRecords= repository.updateAllColumnById(entity);

            if(effectRecords == 0){
                throw new OptimisticConcurrencyException();
            }
        }

        if(detailEntityList !=null){
            detailEntityList.forEach(p->{
                if(StringUtils.isEmpty(p.getId())){
                    p.setId(IdGenerator.get());
                    p.setLoanProductTypeId(entity.getId());
                    subTypeRepository.insert(p);
                }else {
                    subTypeRepository.updateAllColumnById(p);
                }
            });
        }

        if(deletedDetailList != null && deletedDetailList.size()>0){
            subTypeRepository.deleteBatchIds(deletedDetailList);
        }
        return entity.getId();
    }

    @Override
    @Transactional
    public void delete(String id) {

        EntityWrapper<PL_LoanProductSubType> detailEW = new EntityWrapper<PL_LoanProductSubType>();
        detailEW.eq("loanProductTypeId",id);
        subTypeRepository.delete(detailEW);

        repository.deleteById(id);

    }

    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        ids.forEach(p -> {
            delete(p);
        });
    }
}
