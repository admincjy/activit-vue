package com.wl.wlp2ploansystem.infrastructures.common.ueditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UeditorController {
    @Autowired
    private ActionEnter actionEnter;

    @ResponseBody
    @RequestMapping(value="/ueditor/exec",produces="application/javascript;charset=utf-8")
    public String exe(HttpServletRequest request){
        return actionEnter.exec(request);
    }

}
