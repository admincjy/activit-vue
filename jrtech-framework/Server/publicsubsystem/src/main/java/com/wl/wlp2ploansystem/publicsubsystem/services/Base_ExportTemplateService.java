package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_ExportTemplate;

import java.util.List;

public interface Base_ExportTemplateService extends BaseService {

    Base_ExportTemplate get(String id);

    String save(Base_ExportTemplate input);

    void delete(String id);

    void batchDelete(List<String> ids);

    List<Base_ExportTemplate> getList(EntityWrapper<Base_ExportTemplate> ew);
}
