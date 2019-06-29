package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Attachment;
import com.wl.wlp2ploansystem.publicsubsystem.entities.keyIntegerValuePair;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface Base_AttachmentRepository  extends BaseRepository<Base_Attachment> {
    List<keyIntegerValuePair> getCategoryRowsCount(@Param("ew")  EntityWrapper<Base_Attachment> ew);
}

