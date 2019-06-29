package com.wl.wlp2ploansystem.publicsubsystem.manager;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
public class Base_UserLoginAttemptManager {

    private  String loginFailedCacheKey  = "";

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        loginFailedCacheKey =  applicationProperties.getApplicationName() + ":Base_UserLoginAttemptManager:loginFailed:";
    }

    public String getCacheKey() {
        return loginFailedCacheKey;
    }


    public void reloadCache() {
        redisTemplate.delete(loginFailedCacheKey+"*");
    }

    public int setAndGetLoginFailed(String loginId){

        String lowercaseLoginId = loginId.toLowerCase(Locale.ENGLISH);
        String redisKey = this.loginFailedCacheKey + lowercaseLoginId;

        BoundValueOperations<String,Integer> valueOperations = redisTemplate.boundValueOps(redisKey);
        valueOperations.increment(1);
        valueOperations.expire(10, TimeUnit.MINUTES); //十分钟后过期

        return  valueOperations.get().intValue();
    }
    public int getLoginFailed(String loginId){

        String lowercaseLoginId = loginId.toLowerCase(Locale.ENGLISH);
        String redisKey = this.loginFailedCacheKey + lowercaseLoginId;

        BoundValueOperations<String,Integer> valueOperations = redisTemplate.boundValueOps(redisKey);
        return  valueOperations.get().intValue();
    }
    public void deleteLoginFailed(String loginId){
        String lowercaseLoginId = loginId.toLowerCase(Locale.ENGLISH);
        String redisKey = this.loginFailedCacheKey + lowercaseLoginId;
        redisTemplate.delete(redisKey);
    }
}