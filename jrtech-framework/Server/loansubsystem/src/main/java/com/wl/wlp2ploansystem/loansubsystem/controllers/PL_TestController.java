package com.wl.wlp2ploansystem.loansubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.loansubsystem.controllers.dto.PL_Test;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/authapi/pl_test",produces = {MediaType.APPLICATION_JSON_VALUE})
public class PL_TestController {


    @PostMapping("/test1")
    @Log("测试方法1")
    @PreAuthorize("hasAuthority('pl_test_test1')")
    public PL_Test test1(@RequestBody @Valid PL_Test testInput){

        PL_Test newDto = new PL_Test();
        newDto.setId(testInput.getId() + "_hello");
        newDto.setName(testInput.getName() + "_hello");

        return  newDto;
    }

    @PostMapping("/test2")
    @Log("测试方法2")
    @PreAuthorize("hasAuthority('menu_base_test1')")
    public void test2(){
    }
}
