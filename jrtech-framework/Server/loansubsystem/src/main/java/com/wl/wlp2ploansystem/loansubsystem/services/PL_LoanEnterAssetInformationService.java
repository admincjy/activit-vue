package com.wl.wlp2ploansystem.loansubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;

import java.util.List;

public interface PL_LoanEnterAssetInformationService extends BaseService {

    PL_LoanEnterAssetInformation get(String id);

    String save(PL_LoanEnterAssetInformation input);

    void delete(String id);

    void batchDelete(List<String> ids);

    Page<PL_LoanEnterAssetInformation> getPagedList(Page<PL_LoanEnterAssetInformation> pager, EntityWrapper<PL_LoanEnterAssetInformation> ew);

    List<PL_LoanEnterAssetInformation> getList(Wrapper<PL_LoanEnterAssetInformation> ew);
}

 