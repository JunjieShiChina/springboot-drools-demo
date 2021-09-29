package me.shijunjie.springbootdroolsdemo.controller;

import io.swagger.annotations.Api;
import me.shijunjie.springbootdroolsdemo.utils.KieUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.utils.KieHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rule")
@Api("RuleController")
public class RuleController {

    @GetMapping("/reload")
    public String reload() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rules/temp.drl", loadRules());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }
        KieUtils.setKieContainer(kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId()));

        return "success";
    }

    @GetMapping("/reloadByHelper")
    public String reloadByHelper() {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(loadRules(), ResourceType.DRL);

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieContainer kieContainer = kieHelper.getKieContainer();
        KieUtils.setKieContainer(kieContainer);

        return "success";
    }

    private String loadRules() {
        // 从数据库加载的规则
        return "package plausibcheck.adress\n\n rule \"Postcode 6 numbers\"\n\n    when\n  then\n        System.out.println(\"规则2中打印日志：校验通过!\");\n end";

    }

}
