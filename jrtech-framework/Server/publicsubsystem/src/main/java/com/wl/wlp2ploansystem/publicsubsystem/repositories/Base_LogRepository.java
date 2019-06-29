package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Base_LogRepository  extends BaseRepository<Base_Log> {

    Base_Log get(@Param("id")  String id);
    List<Base_Log> getList(Page<Base_Log> page, @Param("ew")Wrapper<Base_Log> ew);

}
