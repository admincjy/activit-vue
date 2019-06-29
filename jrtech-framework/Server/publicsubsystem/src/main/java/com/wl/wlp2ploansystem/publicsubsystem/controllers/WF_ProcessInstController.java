package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.WF_ProcessinstQueryInDto;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInst;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstHistoryFull;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstFull;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_ProcessInstService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/authapi/wf_ProcessInst")
public class WF_ProcessInstController {

    @Autowired
    private WF_ProcessInstService processInstService;

    @Autowired
    private WF_WorkflowService workflowService;

    /**
     * <p>
     * 根据 input 条件，分页读取流程实例
     * </p>
     *
     * @param input 分页,查询条件对象
     * @return 流程实例分页对象
     */
    @PostMapping("/getPagedList")
    @Log("分页读取流程实例列表")
    @PreAuthorize("hasAuthority('menu_wf_processinstlist')")
    public PagedResultOutput<WF_ProcessInstFull> getPagedList(@Valid @RequestBody WF_ProcessinstQueryInDto input) {
        EntityWrapper<WF_ProcessInstFull> ew = new EntityWrapper<>();
        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        ew.ge(input.getStartDateBegin() != null,"startDate",input.getStartDateBegin());
        if(input.getStartDateEnd() != null) {
            ew.lt("startDate", input.getStartDateEnd().plusDays(1));
        }
        ew.eq(!StringUtils.isEmpty(input.getProcessDefinationKey()),"processDefinationKey",input.getProcessDefinationKey());

        if (!StringUtils.isEmpty(input.getSearchKey())) {
            ew.andNew().like("businessId", input.getSearchKey())
                    .or().
                    like("origiatorName", input.getSearchKey())
                    .or().
                    like("folio", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }else{
            ew.orderBy("id", false);
        }

        Page<WF_ProcessInstFull> pager = new Page<WF_ProcessInstFull>(input.getSkipCount(), input.getMaxResultCount());

        Page<WF_ProcessInstFull> results = processInstService.getPagedList(pager, ew);

        return new PagedResultOutput<WF_ProcessInstFull>(results.getTotal(), results.getRecords());

    }
    /**
     * <p>
     * 删除流程实例
     * </p>
     *
     * @param id 主键
     */
    @PostMapping("/delete")
    @Log("删除流程实例")
    @PreAuthorize("hasAuthority('menu_wf_processinstlist')")
    public void delete(String id) {
        workflowService.deleteProcessInstance(id,"管理删除");
    }
    /**
     * <p>
     * 批量删除流程实例
     * </p>
     *
     * @param ids 主键列表
     */
    @PostMapping("/batchDelete")
    @Log("批量删除流程实例")
    @PreAuthorize("hasAuthority('menu_wf_processinstlist')")
    public void batchDelete(@RequestBody List<String> ids) {
        ids.forEach(id->{
            delete(id);
        });
    }



}
