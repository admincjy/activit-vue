package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authapi/base_Common")
@Display("通用服务")
public class Base_CommonController {

    @RequestMapping(value = "/getSUIds", method = RequestMethod.POST)
    @Log(disabled = true)
    public List<String> getSUIds(Integer count){
        List<String> list = new ArrayList<>(count);

        for(int i=0;i<count;i++){
            list.add(IdGenerator.get());
        }

        return list;
    }
}
