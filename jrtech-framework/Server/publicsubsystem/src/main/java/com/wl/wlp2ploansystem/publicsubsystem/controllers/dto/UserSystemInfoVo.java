package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode()
public class UserSystemInfoVo {

    public Integer getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Integer notificationCount) {
        this.notificationCount = notificationCount;
    }

    public Collection<TreeObject<Base_Navigation>> getAuthoritiyNavigationTree() {
        return authoritiyNavigationTree;
    }

    public void setAuthoritiyNavigationTree(Collection<TreeObject<Base_Navigation>> authoritiyNavigationTree) {
        this.authoritiyNavigationTree = authoritiyNavigationTree;
    }
    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }
    private  Collection<TreeObject<Base_Navigation>> authoritiyNavigationTree;
    private  Integer notificationCount;

    @ApiModelProperty("功能权限列表")
    private Collection<String> permissions;
}
