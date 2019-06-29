package com.wl.wlp2ploansystem.infrastructures.common.clientJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wl.wlp2ploansystem.infrastructures.common.jsscript.ScriptManager;
import com.wl.wlp2ploansystem.infrastructures.common.support.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成前端json ScriptManager
 */
@Component
public class ClientJsonScriptManager implements ScriptManager {


    @Autowired
    private Collection<ClientJsonManager> clientJsonManagers;

    @Override
    public String getScript() {

        StringBuilder sb =new StringBuilder();

        sb.append("(function() { \n");


        sb.append("    tapp.data = \n");

        Map<String,Object> jsons = new HashMap<>();

        clientJsonManagers.forEach(p -> {
            jsons.putAll(p.getClientJsonObject());
        });

        try {
            String jsonString = JsonHelper.map2Json(jsons,true);
            sb.append(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sb.append("})();\n");

        return sb.toString();

    }
}
