package com.wl.wlp2ploansystem.loansubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.loansubsystem.entities.*;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanDocFullRepository;
import com.wl.wlp2ploansystem.loansubsystem.repositories.PL_LoanDocRepository;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanDocService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterCompanyService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterGXDService;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PL_LoanDocServiceImpl extends BaseServiceImpl implements PL_LoanDocService {

    @Autowired
    private PL_LoanDocRepository repository;

    @Autowired
    private PL_LoanDocFullRepository docFullRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PL_LoanDocFull> getFullPagedList(Page<PL_LoanDocFull> pager, EntityWrapper<PL_LoanDocFull> ew) {
        if (null != ew) {
            ew.orderBy(pager.getOrderByField(), pager.isAsc());
        }

        return pager.setRecords(docFullRepository.selectPage(pager, ew));
    }
    @Override
    @Transactional(readOnly = true)
    public List<PL_LoanDoc> getList(EntityWrapper<PL_LoanDoc> ew) {

        return repository.selectList(ew);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<PL_LoanDoc> getPagedList(Page<PL_LoanDoc> pager, EntityWrapper<PL_LoanDoc> ew) {
        if (null != ew) {
            ew.orderBy(pager.getOrderByField(), pager.isAsc());
        }
        return pager.setRecords(repository.selectPage(pager, ew));
    }
    @Override
    @Transactional(readOnly = true)
    public PL_LoanDoc get(String id) {
        PL_LoanDoc basicDocEntity = repository.selectById(id);
        return  basicDocEntity;
    }
    @Override
    @Transactional(readOnly = true)
    public PL_LoanDocFull getFull(String id) {
        PL_LoanDocFull basicDocEntity = docFullRepository.selectById(id);
        return  basicDocEntity;
    }

    @Override
    @Transactional
    public void save(PL_LoanDoc input) {

        Integer effectRecords = repository.updateAllColumnById(input);
        if(effectRecords == 0){
            throw new OptimisticConcurrencyException();
        }
    }
    @Override
    @Transactional
    public void delete(String id) { 
        repository.deleteById(id);
    }
}
