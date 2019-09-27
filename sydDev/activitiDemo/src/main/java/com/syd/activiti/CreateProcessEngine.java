package com.syd.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/19 下午3:11
 */
public class CreateProcessEngine {
    public static void main(String[] args) {
        //创建流程引擎
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("spring-activiti.xml")
                .buildProcessEngine();
        System.out.println("流程:"+processEngine);
        //部署BPMN到流程引擎
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("证据集司法鉴定流程")
                .addClasspathResource("bpmn/testProcess.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println(
                "Found process definition ["
                        + processDefinition.getName() + "] with id ["
                        + processDefinition.getId() + "]");
        //启动流程
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("process");

        /*TaskService taskService = processEngine.getTaskService();
        String assiginee = "zhiFaP";

        Map<String,Object> veriables = new HashMap<String, Object>();
        veriables.put("chuangjian","申请司法鉴定");
        taskService.complete("57509",veriables);
        System.out.println("完成ID为57509的任务");
        List<Task> list = taskService.createTaskQuery().taskAssignee(assiginee).list();
        for (Task task: list){
            System.out.println("任务ID："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务的创建时间："+task.getCreateTime());
            System.out.println("任务办理人："+task.getAssignee());
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("执行对象ID："+task.getExecutionId());
            System.out.println("流程定义ID："+task.getProcessDefinitionId());
            System.out.println("#############################################");
        }*/
    }
}
