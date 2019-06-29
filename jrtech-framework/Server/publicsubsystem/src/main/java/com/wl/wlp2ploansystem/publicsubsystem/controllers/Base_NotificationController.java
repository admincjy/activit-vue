package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Notification;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_notification",produces = {MediaType.APPLICATION_JSON_VALUE})

public class Base_NotificationController {
    @Autowired
    private Base_NotificationService service;



    @PostMapping("/getList")
    @Display("分页获取消息列表")
    @Log("分页获取消息列表")
    // @PreAuthorize("hasAuthority('permission_03')")
    public PagedResultOutput<Base_Notification> getList(@Valid @RequestBody CommonPagedInputDto input) {
        EntityWrapper<Base_Notification> ew = new EntityWrapper<>();
        ew.eq("`read`","0");
        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getSearchKey())) {
            ew.andNew().like("title", input.getSearchKey());
        }

        ew.andNew().like("targetTos",","+ SecurityUtils.getCurrentUser().getId()+",")
                .or().like("targetTos",SecurityUtils.getCurrentUser().getId()+",", SqlLike.RIGHT)
                .or().like("targetTos",","+ SecurityUtils.getCurrentUser().getId(), SqlLike.LEFT)
                .or().eq("targetType",Base_Notification.targetType_all);

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_Notification> pager = new Page<Base_Notification>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_Notification> results = service.getPagedList(pager, ew);

        return new PagedResultOutput<Base_Notification>(results.getTotal(), results.getRecords());

    }
    @PostMapping("/get")
    @Log("读取消息信息")
    public Base_Notification get(String id) {
        return service.get(id);
    }
    @PostMapping("/save")
    @Log("新增消息并通知")
    @PreAuthorize("hasAuthority('gl_notification_send')")
    public void saveAndNotify(@RequestBody  Base_Notification input) {
      service.saveAndNotify(input);

    }
    @PostMapping("/delete")
    @Log("删除消息信息")
    public void delete(String id) {
        service.delete(id);
    }
    @PostMapping("/batchDelete")
    @Log("批量删除消息信息")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
    @PostMapping("/read")
    @Log("设置消息为已读")
    public void read(@RequestBody List<String> ids) {
           service.read(ids);
    }

    @PostMapping("/readAll")
    @Log("设置消息为已读")
    public void readAll() {
           service.readAll();
    }

}
