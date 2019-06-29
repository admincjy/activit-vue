package com.wl.wlp2ploansystem.infrastructures.common.cache;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
* 缓存管理组件
* */
@Component
public class AppCacheManger {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Collection<AppCache> appCache;

    @Autowired
    ApplicationProperties applicationProperties;

    /**
     * 刷新缓存
     */

    public  void reload(){
        String cache_prefix=  applicationProperties.getApplicationName() +  ":wl_app_cache:";
        appCache.forEach(p-> {
            p.initCacheKey(cache_prefix);
            p.reloadCache();
            String cacheKey = p.getCacheKey();
            logger.info("appCache:" +cacheKey + " cache load success!");
        });
    }

}
