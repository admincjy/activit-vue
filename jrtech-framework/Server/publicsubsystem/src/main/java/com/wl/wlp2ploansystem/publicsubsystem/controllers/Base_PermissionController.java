package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Permission;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/authapi/base_Permission",produces = {MediaType.APPLICATION_JSON_VALUE})
public class Base_PermissionController {
    @Autowired
    private Base_PermissionService service;

    @RequestMapping(value = "/getPermissions", method = RequestMethod.POST)
    @Log("读取权限列表")
    public Collection<TreeObject<Base_Permission>> getPermissions(){
        return service.getPermissions();
    }

    /***
     * 更新父节点和序号
     * @param inputDto 输入Dto
     */
    @PostMapping("/updateParentIdAndSortIndex")
    @Log("更新父节点及序号")
    @PreAuthorize("hasAuthority('menu_base_permissiontree')")
    public void updateParentIdAndSortIndex(@Valid @RequestBody TreeNodeDropInputDto inputDto){
        service.updateParentIdAndSortIndex(inputDto);
    }

    /**
     * <p>
     * 根据id，读取功能权限
     * </p>
     *
     * @param id 主键
     * @return 功能权限对象
     */
    @PostMapping("/get")
    @Log("读取功能权限对象")
    @PreAuthorize("hasAuthority('menu_base_permissiontree')")
    public Base_Permission get(String id){

        return  service.get(id);
    }

    /**
     * <p>
     * 新增/修改功能权限
     * </p>
     *
     * @param input 功能权限对象
     * @return HashMap<String,String>存储id信息:
     */
    @PostMapping("/save")
    @Log("新增/修改功能权限")
    @PreAuthorize("hasAuthority('menu_base_permissiontree')")
    public Base_Permission save(@Valid @RequestBody Base_Permission input) {
        String id =  service.save(input);

        return this.get(id);
    }

    /**
     * <p>
     * 删除功能权限
     * </p>
     *
     * @param id 主键
     */
    @PostMapping("/delete")
    @Log("删除功能权限")
    @PreAuthorize("hasAuthority('menu_base_permissiontree')")
    public void delete(String id) {
        service.delete(id);
    }
    /**
     * <p>
     * 批量删除功能权限
     * </p>
     *
     * @param ids 主键列表
     */
    @PostMapping("/batchDelete")
    @Log("批量删除功能权限")
    @PreAuthorize("hasAuthority('menu_base_permissiontree')")
    public void batchDelete(@RequestBody List<String> ids) {
        service.batchDelete(ids);
    }
}
