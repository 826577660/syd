package com.syd.activiti;

import org.activiti.engine.*;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/20 下午3:31
 */
public class TestProcess {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();
    FormService formService = processEngine.getFormService();

    @Test
    public void getFormInfo(){
        String tasksId = "90002";
        TaskFormData data = formService.getTaskFormData(tasksId);
        System.out.println("表单名--"+data.getTask().getName());
        List<FormProperty> properties=data.getFormProperties();
        Map<String,Object> variables = new HashMap<String,Object>();
        for (FormProperty property:properties){
            //variables.put(property.getId(),property.getName());
            System.out.println("表单数据Id--"+property.getId()+"    表单数据Name--"+property.getName());
        }

    }

    String taskId = "";
    @Test
    public void getTaskInfo(){

        //String assiginee = "jianDingP";
        //String assiginee = "shangWuP";
        //String assiginee = "jianDingGuanLiP";
        String assiginee = "zhiFaP";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assiginee).list();
        for (Task task: list){
            taskId=task.getId();
            System.out.println("任务ID："+taskId);
            System.out.println("任务名称："+task.getName());
            System.out.println("任务的创建时间："+task.getCreateTime());
            System.out.println("任务办理人："+task.getAssignee());
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("执行对象ID："+task.getExecutionId());
            System.out.println("流程定义ID："+task.getProcessDefinitionId());
            System.out.println("#############################################");
        }
        Map<String,Object> map=taskService.getVariables(taskId);

        System.out.println("获取上个任务的变量：");
        System.out.println(map);
    }

    @Test
    public void complateTask(){
        String taskId = "90002";
        TaskFormData data = formService.getTaskFormData(taskId);
        System.out.println("表单名--"+data.getTask().getName());
        List<FormProperty> properties=data.getFormProperties();
        Map<String,Object> variables = new HashMap<String,Object>();
        for (FormProperty property:properties){
            variables.put(property.getId(),property.getName());
            System.out.println("表单数据Id--"+property.getId()+"    表单数据Name--"+property.getName());
        }
        //variables.put("fenpei","确认签发");
        taskService.complete(taskId,variables);
        System.out.println("完成ID为"+taskId+"的用户任务");
    }

    @Test
    public void updateVariable(){
        taskService.setVariable("80003","shangshen","商审通过");
    }

    @Test
    public void deleteProcess(){
        runtimeService.deleteProcessInstance("102504","想删除");
    }
}
