package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_SequenceNumber;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Base_SequenceNumberRepository extends BaseRepository<Base_SequenceNumber> {
    List<String> getSequenceNumbers(@Param("key") String key, @Param("count") Integer count);
}

