package com.wl.wlp2ploansystem.publicsubsystem.manager;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.cache.AppCache;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Permission;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithAuthorities;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Base_UserWithAuthoritiesCacheManager implements AppCache {

    private  String cacheKey  = "";

    @Autowired
    private Base_UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void initCacheKey(String cachePrefix) {
        cacheKey =  cachePrefix + "Base_UserWithAuthoritiesManager";
    }

    @Override
    public String getCacheKey() {
        return cacheKey;
    }

    @Override
    public void reloadCache() {
        innerReloadCache(null);
    }
    public void innerReloadCache(EntityWrapper<Base_UserWithAuthorities> userWithAuthoritiesEntityWrapper) {

        List<Base_UserWithAuthorities> users = userRepository.getUserWithAuthoritiesList(userWithAuthoritiesEntityWrapper);
        users.stream().forEach(p->{
            if(p.getGrantedAuthorities() == null){
                p.setGrantedAuthorities(new ArrayList<String>());
            }
            //如果需要用户首次登陆时变更密码
            if(p.getShouldChangePassword() == null || Boolean.TRUE.equals(p.getShouldChangePassword())){
                p.getGrantedAuthorities().clear();
                p.getGrantedAuthorities().add(Base_Permission.Base_Permission_Common);
            }
            if(!p.getGrantedAuthorities().contains(Base_Permission.Base_Permission_Common)){
                p.getGrantedAuthorities().add(Base_Permission.Base_Permission_Common);
            }
        });

        Map<String,Base_UserWithAuthorities> userWithAuthoritiesMap = users.stream().collect(Collectors.toMap(Base_UserWithAuthorities::getLowerCaseLoginId, item -> item));
        HashOperations<String, String, Base_UserWithAuthorities> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll(cacheKey,userWithAuthoritiesMap);
    }

    public Map<String,Base_UserWithAuthorities> getAll(){

        if(!redisTemplate.hasKey(cacheKey)){
            this.reloadCache();
        }
        HashOperations<String, String, Base_UserWithAuthorities> opsForHash = redisTemplate.opsForHash();
        Map<String,Base_UserWithAuthorities> userWithAuthoritiesMap = opsForHash.entries(cacheKey);
        return  userWithAuthoritiesMap;
    }

    public Base_UserWithAuthorities get(String loginId) {


        if(!redisTemplate.hasKey(cacheKey)){
            this.reloadCache();
        }
        String lowercaseLoginId = loginId.toLowerCase(Locale.ENGLISH);
        HashOperations<String, String, Base_UserWithAuthorities> opsForHash = redisTemplate.opsForHash();
        Base_UserWithAuthorities userWithAuthorities = opsForHash.get(cacheKey,lowercaseLoginId);
        return  userWithAuthorities;
    }

    /***
     * 注册缓存变更事件，更新缓存
     * @param entityChangedEvent 缓存变更事件对象
     */
    @EventListener
    public void register(EntityChangedEvent entityChangedEvent)
    {
        String docType = entityChangedEvent.getDocType();
        if(!"base_userWithAuthorities".equals(docType)){
            return;
        }
        if(entityChangedEvent.getDocIds() == null){
            this.reloadCache();
        }else {
            EntityWrapper<Base_UserWithAuthorities> userWithAuthoritiesEntityWrapper = new EntityWrapper<>();
            userWithAuthoritiesEntityWrapper.in("id", entityChangedEvent.getDocIds());
            this.innerReloadCache(userWithAuthoritiesEntityWrapper);
        }
    }


}
