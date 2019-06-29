package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelationFull;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanEnterCustomerRelationRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterCustomerRelationService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PL_LoanEnterCustomerRelationServiceImpl extends BaseServiceImpl implements PL_LoanEnterCustomerRelationService {


    @Autowired
    private PL_LoanEnterCustomerRelationRepository repository;


    @Transactional(readOnly = true)
    public Page<PL_LoanEnterCustomerRelation> getPagedList(Page<PL_LoanEnterCustomerRelation> page, EntityWrapper<PL_LoanEnterCustomerRelation> ew) {

        if (null != ew) {
            ew.orderBy(page.getOrderByField(), page.isAsc());
        }
        return page.setRecords(repository.selectPage(page, ew));

    }

    @Override
    @Transactional(readOnly = true)
    public PL_LoanEnterCustomerRelation get(String id) {
        PL_LoanEnterCustomerRelation entity = repository.selectById(id);
        return entity;
    }

    @Override
    @Transactional
    public String save(PL_LoanEnterCustomerRelation input ) {
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

    @Override
    @Transactional(readOnly = true)
    public List<PL_LoanEnterCustomerRelationFull> getList(Wrapper<PL_LoanEnterCustomerRelationFull> ew) {

        return repository.getList(ew);

    }
    @Override
    @Transactional(readOnly = true)
    public PL_LoanEnterCustomerRelationFull getFull(String id) {
        PL_LoanEnterCustomerRelationFull entity = repository.getFull(id);
        return entity;
    }
}