package com.wl.wlp2ploansystem.loansubsystem.repositories;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelation;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterCustomerRelationFull;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PL_LoanEnterCustomerRelationRepository extends BaseRepository<PL_LoanEnterCustomerRelation> {

    List<PL_LoanEnterCustomerRelationFull> getList(@Param("ew")Wrapper<PL_LoanEnterCustomerRelationFull> ew);

    PL_LoanEnterCustomerRelationFull getFull(@Param("id")String  id);
}
