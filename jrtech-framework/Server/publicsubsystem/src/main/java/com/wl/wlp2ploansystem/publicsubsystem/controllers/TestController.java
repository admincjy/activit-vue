package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_UserRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_OrganizationService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/pmtapi/test")
public class TestController {

    @Autowired
    private Base_UserRepository userRepository;

    @Autowired
    private Base_OrganizationService orgService;

    @Autowired
    private Base_UserService userService;


    @GetMapping(value = "/getOrganizationTreeChildren")
    public Collection<TreeObject<Base_Organization>> getOrganizationTreeChildren(String parentId){
        return  orgService.getOrganizationTreeChildren(parentId);
    }
    @PostMapping("/saveOrg")
    public Base_Organization saveOrg(@RequestBody Base_Organization input) {


        String id =  orgService.save(input);

        return  orgService.get(id);
    }

    @GetMapping("/deleteOrg")
    public void deleteOrg(String id) {
        orgService.delete(id);
    }

    @GetMapping("/getUser")
    public Base_User getUser(String id) {

        return  userService.get(id);
    }
    @GetMapping("/deleteUser")
    public void deleteUser(String id) {
        userService.delete(id);
    }

    @PostMapping("/batchDeleteUser")
    public void batchDeleteUser(@RequestBody List<String> ids) {
        userService.batchDelete(ids);
    }
    @PostMapping("/getUsers")
    public PagedResultOutput<Base_User> getUsers(@Valid @RequestBody CommonCategoryPagedInputDto input) {
        EntityWrapper<Base_User> ew = new EntityWrapper<>();
        ew.eq("organizationId",input.getCategoryId());

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("loginId", input.getSearchKey())
                    .or().
                    like("name", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_User> pager = new Page<Base_User>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_User> results = userService.getUserPagedList(pager, ew);

        results.getRecords().forEach(p -> {
            p.setLoginPassword(null);
        });
        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

}
