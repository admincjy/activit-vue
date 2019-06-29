package com.wl.wlp2ploansystem.publicsubsystem.controllers;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonSearchInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.PagedResultOutput;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.Base_UserAdminChangePasswordDto;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.Base_UserChangePasswordDto;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.Base_UserRoleCategoryUsersInputDto;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithDepartmentNames;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithDepartments;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithRoleIds;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/authapi/base_user")
@Display("用户服务")
public class Base_UserController {

    @Autowired
    private Base_UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/getAllUsers")
    @Display("分页获取用户列表")
    @Log("读取用户列表")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public PagedResultOutput<Base_UserWithDepartmentNames> getAllUsers(@Valid @RequestBody CommonPagedInputDto input) {
        EntityWrapper<Base_UserWithDepartmentNames> ew = new EntityWrapper<>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());

        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("loginId", input.getSearchKey())
                    .or().
                    like("name", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_UserWithDepartmentNames> pager = new Page<Base_UserWithDepartmentNames>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_UserWithDepartmentNames> results = userService.getPagedList(pager, ew);

        results.getRecords().forEach(p -> {
            p.setLoginPassword(null);
        });
        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

    @PostMapping("/getRoleCategoryUsers")
    @Display("分页获取岗位用户列表")
    @Log("分页获取岗位用户列表")
    public PagedResultOutput<Base_User> getRoleCategoryUsers( @RequestBody Base_UserRoleCategoryUsersInputDto input) {
        EntityWrapper<Base_User> ew = new EntityWrapper<>();

        CommonSearchInputDto.resolveFilters(ew,input.getFilters());
//        ew.and().eq("activited","1");

        if (!StringUtils.isEmpty(input.getSearchKey())) {

            ew.andNew().like("a.loginId", input.getSearchKey())
                    .or().
                    like("a.name", input.getSearchKey());
        }

        if (!StringUtils.isEmpty(input.getSortingFiled())) {
            ew.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
        }

        Page<Base_User> pager = new Page<Base_User>(input.getSkipCount(), input.getMaxResultCount());

        Page<Base_User> results = userService.getPagedCurrDepartmentRoleCategoryUsers(pager,input.getRoleCategoryId(), ew);

        results.getRecords().forEach(p -> {
            p.setLoginPassword(null);
        });
        return new PagedResultOutput<>(results.getTotal(), results.getRecords());

    }

    @PostMapping("/getUser")
    @Log("读取用户明细")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public Base_UserWithRoleIds getUser(String id) {
        Base_UserWithRoleIds result =  userService.findOneWithRoleIds(id);
        if(result !=null){
            result.setLoginPassword(null);
        }

        return result;
    }
    @PostMapping("/getUserWithDepartments")
    @Log("读取用户及部门信息")
    public Base_UserWithDepartments getUserWithDepartments(String id) {
        Base_UserWithDepartments result =  userService.getUserWithDepartments(id);
        if(result !=null){
            result.setLoginPassword(null);
        }
        return result;
    }
    @PostMapping("/getCurrentUserInfo")
    @Log("读取当前用户信息")
    public DomainUserDetails getCurrentUserInfo() {
        DomainUserDetails domainUserDetails= SecurityUtils.getCurrentUser();
        domainUserDetails.setLoginPassword(null);
        return  domainUserDetails;
    }

    @PostMapping("/batchInsert")
    @Log("批量添加用户")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public List<String> batchInsert(@RequestBody @Valid List<Base_UserWithRoleIds> input) {
        return userService.batchInsert(input);

    }

    @PostMapping("/saveUser")
    @Log("添加/修改用户")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public HashMap<String,String> saveUser(@RequestBody  Base_UserWithRoleIds input) {

        if (StringUtils.isEmpty(input.getId())) {
            input.setLoginPassword(passwordEncoder.encode(input.getLoginPassword()));
        }
        String id =  userService.saveUser(input);

        HashMap<String,String> result = new HashMap<String,String>();
        result.put("id",id);

        return result;
    }

    @PostMapping("/delete")
    @Log("删除用户")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public void delete(String id) {
        userService.delete(id);
    }

    @PostMapping("/batchDelete")
    @Log("批量删除用户")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public void batchDelete(@RequestBody List<String> ids) {
        userService.batchDelete(ids);
    }

    @PostMapping("/changePassword")
    @Log("修改密码")
    public void changePassword(@RequestBody Base_UserChangePasswordDto input) {
        Base_User entity = userService.get(SecurityUtils.getCurrentUser().getId());

        if (!passwordEncoder.matches(input.getOldPassword(), entity.getLoginPassword())) {
            throw new UserFriendlyException("原密码输入错误！");
        }

        String encodeNewPassword = passwordEncoder.encode(input.getNewPassword());
        userService.changePassword(SecurityUtils.getCurrentUser().getId(),
                encodeNewPassword);
    }

    @PostMapping("/adminChangePassword")
    @Log("管理员更改密码")
    @PreAuthorize("hasAuthority('menu_base_user')")
    public void adminChangePassword(@RequestBody Base_UserAdminChangePasswordDto input) {

        String encodeNewPassword = passwordEncoder.encode(input.getNewPassword());
        userService.adminChangePassword(input.getId(),
                encodeNewPassword,input.getShouldChangePassword());
    }

    @PostMapping("/modifyMyProfile")
    @Log("修改个人信息")
    public void modifyMyProfile(@RequestBody Base_User input) {
        input.setId(SecurityUtils.getCurrentUser().getId());
        userService.updateUser(input);
    }
}
