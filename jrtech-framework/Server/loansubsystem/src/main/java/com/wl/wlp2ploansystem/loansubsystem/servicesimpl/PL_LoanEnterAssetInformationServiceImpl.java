package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanEnterAssetInformationRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterAssetInformationService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PL_LoanEnterAssetInformationServiceImpl extends BaseServiceImpl implements PL_LoanEnterAssetInformationService {


    @Autowired
    private PL_LoanEnterAssetInformationRepository repository;
 

    @Transactional(readOnly = true)
    public Page<PL_LoanEnterAssetInformation> getPagedList(Page<PL_LoanEnterAssetInformation> page, EntityWrapper<PL_LoanEnterAssetInformation> ew) {

        if (null != ew) {
            ew.orderBy(page.getOrderByField(), page.isAsc());
        }
        return page.setRecords(repository.selectPage(page, ew));

    }
    @Override
    @Transactional(readOnly = true)
    public List<PL_LoanEnterAssetInformation> getList(Wrapper<PL_LoanEnterAssetInformation> ew) {

        return repository.selectList(ew);

    }
    @Override
    @Transactional(readOnly = true)
    public PL_LoanEnterAssetInformation get(String id) {
        PL_LoanEnterAssetInformation entity = repository.selectById(id); 
        return entity;
    }

    @Override
    @Transactional
    public String save(PL_LoanEnterAssetInformation input ) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
            repository.insert(input);
        } else {
            repository.updateAllColumnById(input);
        }
        return input.getId();
    }

    @Override
    @Transactional
    public void delete(String id) {
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