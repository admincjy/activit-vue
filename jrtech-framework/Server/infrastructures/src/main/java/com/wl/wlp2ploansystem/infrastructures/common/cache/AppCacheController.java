package com.wl.wlp2ploansystem.infrastructures.common.cache;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* 缓存管理Controller
* */
@RestController
@Display("缓存管理服务")
@RequestMapping("/authapi/appCache")
public class AppCacheController {

    @Autowired
    private AppCacheManger appCacheManger;

    /**
     * 刷新缓存
     */
    @PostMapping("/reloadCache")
    @Display("刷新缓存")
    @PreAuthorize("hasAuthority('menu_base_appcache')")
    public void reloadCache(){
        appCacheManger.reload();
    }

}
