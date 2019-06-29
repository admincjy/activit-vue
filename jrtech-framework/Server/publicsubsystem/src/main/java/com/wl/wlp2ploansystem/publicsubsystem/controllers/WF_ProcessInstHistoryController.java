package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.*;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstHistory;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstHistoryFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_ProcessInstHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping(value = "/authapi/wF_ProcessInstHistories",produces = {MediaType.APPLICATION_JSON_VALUE})
public class WF_ProcessInstHistoryController {
    @Autowired
    private WF_ProcessInstHistoryService service;

    @PostMapping("/getProcessInstHistoryList")
    @Display("分页获取流程审批历史")
    @Log("分页获取流程审批历史")
    public List<WF_ProcessInstHistoryFull> getProcessInstHistoryList(@RequestBody CommonCategoryIdAndSortingDto input) {
        if (StringUtils.isEmpty(input.getCategoryId()))
        {
            return new ArrayList<WF_ProcessInstHistoryFull>();
        }

        EntityWrapper<WF_ProcessInstHistoryFull> eWrapper = new EntityWrapper<WF_ProcessInstHistoryFull>();
        eWrapper.eq("processInstId", input.getCategoryId());

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            eWrapper.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }
        List<WF_ProcessInstHistoryFull>  historyFulls =  service.getList(eWrapper);
        return  historyFulls;
    }

}