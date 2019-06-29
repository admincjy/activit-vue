package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInst;
import com.wl.wlp2ploansystem.publicsubsystem.entities.keyIntegerValuePair;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WF_ProcessInstRepository extends BaseRepository<WF_ProcessInst> {
    List<keyIntegerValuePair> getTaskActUserCount(@Param("processDefinationKey")String processDefinationKey, @Param("actId")String actId);
}
