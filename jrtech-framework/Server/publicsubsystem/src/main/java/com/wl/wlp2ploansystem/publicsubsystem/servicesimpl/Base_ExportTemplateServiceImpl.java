package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ExportTemplate;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_ExportTemplateRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_ExportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class Base_ExportTemplateServiceImpl extends BaseServiceImpl implements Base_ExportTemplateService {


    @Autowired
    private Base_ExportTemplateRepository repository;


    @Transactional(readOnly = true)
    public List<Base_ExportTemplate> getList(EntityWrapper<Base_ExportTemplate> ew) {

        return repository.selectList(ew);

    }

    @Override
    @Transactional(readOnly = true)
    public Base_ExportTemplate get(String id) {
        Base_ExportTemplate entity = repository.selectById(id);
        return entity;
    }

    @Override
    @Transactional
    public String save(Base_ExportTemplate input ) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
        }
        repository.insert(input);

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