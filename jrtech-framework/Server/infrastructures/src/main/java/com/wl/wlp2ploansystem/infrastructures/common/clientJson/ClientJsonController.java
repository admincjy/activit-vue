package com.wl.wlp2ploansystem.infrastructures.common.clientJson;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.cache.AppCache;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.support.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 生成前端json Controller
 */
@RestController
@RequestMapping("/json")
public class ClientJsonController implements AppCache {

    private   String cacheKey =   "";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Collection<ClientJsonManager> clientJsonManagers;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @Log(disabled = true)
    public Map<String,Object> getAll() throws IOException {

        if(redisTemplate.hasKey(cacheKey)){
            String jsonString =   redisTemplate.opsForValue().get(cacheKey).toString();

            return  JsonHelper.json2Map(jsonString);
        }
        else {

          Map<String,Object> jsons = new HashMap<>();

            clientJsonManagers.forEach(p -> {
                jsons.putAll(p.getClientJsonObject());
            });

            String jsonString = JsonHelper.map2Json(jsons,false);
            BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(cacheKey);
            valueOperations.set(jsonString);
            valueOperations.expire(10 * 365, TimeUnit.DAYS); //10年后过期

            return  jsons;
        }
    }

    @Override
    public void initCacheKey(String cachePrefix) {
        cacheKey =  cachePrefix + "ClientJsonController_getall";
    }
    @Override
    public String getCacheKey() {
        return cacheKey;
    }

    @Override
    public void reloadCache() {
        redisTemplate.delete(cacheKey);
    }

    /***
     * 注册缓存变更事件，更新缓存
     * @param entityChangedEvent 缓存变更事件对象
     */
    @EventListener
    public void register(EntityChangedEvent entityChangedEvent)
    {
        /*
        String docType = entityChangedEvent.getDocType();
        if(!"ClientJsonChanged".equals(docType)){
            return;
        }
        this.reloadCache();
        */
    }
}
