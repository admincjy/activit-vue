package com.wl.wlp2ploansystem.infrastructures.common.jsscript;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.cache.AppCache;
import com.wl.wlp2ploansystem.infrastructures.common.clientJson.ClientJsonManager;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * 生成前端js Controller
 */
@RestController
@RequestMapping("/jsscript")
public class JSScriptController implements AppCache {

    private   String cacheKey =   "";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Collection<ScriptManager> scriptManagers;

    @Autowired
    private Collection<ClientJsonManager> clientJsonManagers;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = {"application/x-javascript;charset=utf-8"})
    @Log(disabled = true)
    public String GetAll() {

        if(redisTemplate.hasKey(cacheKey)){
            return  redisTemplate.opsForValue().get(cacheKey).toString();
        }
        else {

            StringBuilder script = new StringBuilder();
            script.append(this.getInitScript());

            scriptManagers.forEach(p -> {
                script.append(p.getScript());
                script.append("\n");
            });

            String scriptString =  script.toString();

            BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(cacheKey);
            valueOperations.set(scriptString);
            valueOperations.expire(10 * 365, TimeUnit.DAYS); //10年后过期

            return  scriptString;
        }
    }

    private  String getInitScript(){
        StringBuilder sb =new StringBuilder();

        sb.append(" var tapp = tapp || {}; \n");
        sb.append("(function() { \n");
        sb.append("  tapp.utils = tapp.utils || {}; \n");
        sb.append("  tapp.utils.createNamespace = function(root, ns) { \n");
        sb.append("     var parts = ns.split('.'); \n");
        sb.append("     for (var i = 0; i < parts.length; i++) { \n");
        sb.append("         if (typeof root[parts[i]] == 'undefined') { \n");
        sb.append("             root[parts[i]] = {}; \n");
        sb.append("         } \n");
        sb.append("         root = root[parts[i]]; \n");
        sb.append("     } \n");
        sb.append("     return root; \n");
        sb.append("   } \n");
        sb.append("})();\n");
        return sb.toString();
    }
    @Override
    public void initCacheKey(String cachePrefix) {
        cacheKey =  cachePrefix + "JSScriptController_getall";
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
        String docType = entityChangedEvent.getDocType();
        if(!"JSScriptChanged".equals(docType)){
            return;
        }
        this.reloadCache();
    }
}