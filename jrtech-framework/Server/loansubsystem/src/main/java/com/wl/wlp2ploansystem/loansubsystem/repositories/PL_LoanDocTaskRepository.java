package com.wl.wlp2ploansystem.loansubsystem.repositories;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanDocTask;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface PL_LoanDocTaskRepository extends BaseRepository<PL_LoanDocTask> {
    List<PL_LoanDocTask> getLoanTaskProgress(Page<PL_LoanDocTask> page
            , @Param("all") boolean all
            , @Param("userIdList")List<String> userIdList
            , @Param("dataPermissionIds")Collection<String> dataPermissionIds
            , @Param("ew")Wrapper<PL_LoanDocTask> ew);
}
