package com.syd.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/23 下午4:12
 */
@Component
public class StartProcess {

    String processId = "";
    ProcessEngine processEngine;
    /*
    启动流程
     */
    public String start(){
        //创建流程引擎
        processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("spring-activiti.xml")
                .buildProcessEngine();
        //部署BPMN到引擎
        processEngine.getRepositoryService().createDeployment().name("测试证据集司法鉴定流程")
                .addClasspathResource("bpmn/testProcess.bpmn20.xml")
                .deploy();

        //启动流程
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("process");
        if (processInstance != null && !processInstance.isEnded()){
            processId = processInstance.getId();
            return processId;
        }else {
            return "流程启动失败";
        }

    }

    /*
    结束历程
     */
    public String shutdown(){
        processEngine.getRuntimeService().deleteProcessInstance(processId,"结束");
        return processId;
    }
    /*
    创建用户和组
     */
    public void saveUserAndGroup(){
        IdentityService identityService = processEngine.getIdentityService();
        //创建组（角色）
        Group group1 = identityService.newGroup("执法人员");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("鉴定管理人员");
        identityService.saveGroup(group2);
        Group group3 = identityService.newGroup("商务员");
        identityService.saveGroup(group3);
        Group group4 = identityService.newGroup("鉴定人");
        identityService.saveGroup(group4);
        //创建用户
        User user1 = identityService.newUser("zhiFaP");
        user1.setEmail("826577660@qq.com");
        identityService.saveUser(user1);
        User user2 = identityService.newUser("jianDingGuanLiP");
        user1.setEmail("82657766@qq.com");
        identityService.saveUser(user2);
        User user3 = identityService.newUser("shangWuP");
        user1.setEmail("8265776@qq.com");
        identityService.saveUser(user3);
        User user4 = identityService.newUser("jianDingP");
        user1.setEmail("826577@qq.com");
        identityService.saveUser(user4);
        //建立用户和角色的关联关系
        identityService.createMembership("zhiFaP","执法人员");
        identityService.createMembership("jianDingGuanLiP","鉴定管理人员");
        identityService.createMembership("shangWuP","商务员");
        identityService.createMembership("jianDingP","鉴定人");
    }
}
