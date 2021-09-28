package me.shijunjie.springbootdroolsdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.shijunjie.springbootdroolsdemo.utils.KieUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api("TestController")
public class TestController {

    @GetMapping("/test")
    @ApiOperation(value = "test", notes = "测试")
    public void test() {
        KieSession kieSession = KieUtils.getKieContainer().newKieSession();
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
