package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.aop.LoggingDetails;
import com.wl.wlp2ploansystem.infrastructures.common.aop.LoggingService;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Log;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_LogRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Base_LogServiceImpl extends BaseServiceImpl implements Base_LogService , LoggingService {

    protected static final Logger logger = LoggerFactory.getLogger(Base_LogServiceImpl.class);

    @Autowired
    private Base_LogRepository repository;


    @Transactional(readOnly = true)
    public Page<Base_Log> getPagedList(Page<Base_Log> page, EntityWrapper<Base_Log> ew) {

        return page.setRecords(repository.getList(page, ew));

    }

    @Override
    @Transactional(readOnly = true)
    public Base_Log get(String id) {
        Base_Log entity = repository.get(id);

        return entity;
    }

    @Override
    @Transactional
    public void insert(Base_Log input) {

        input.setId(IdGenerator.get());
        repository.insert(input);

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
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    @Async
    public void save(LoggingDetails loggingDetails) {
        Base_Log log = new Base_Log();
        log.setUserId(loggingDetails.getUserId());
        log.setOperation(loggingDetails.getOperation());
        log.setMethod(loggingDetails.getMethod());
        log.setParams(loggingDetails.getParams());
        log.setResult(loggingDetails.getResult());
        log.setErrorMessage(loggingDetails.getErrorMessage());
        log.setErrorStackTrace(loggingDetails.getErrorStackTrace());
        log.setExecuteTime(loggingDetails.getExecuteTime());
        log.setDuration(loggingDetails.getDuration());
        log.setIp(loggingDetails.getIp());

        try {
            this.insert(log);
        }
        catch (Exception ex){
            logger.error("日志记录方法失败",ex);
            throw ex;
        }
    }
}

