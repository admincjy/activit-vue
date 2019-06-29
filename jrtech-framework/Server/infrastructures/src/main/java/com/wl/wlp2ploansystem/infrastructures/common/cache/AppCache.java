package com.wl.wlp2ploansystem.infrastructures.common.cache;

/**
* 缓存
* */
public interface AppCache {

    /**
     * 缓存初始化
     * @param cachePrefix 缓存前缀
     */
    void initCacheKey(String cachePrefix);

    /**
     * 获取key
     */
    String getCacheKey();

    /**
     * 重新加载缓存
     */
    void reloadCache();
}
