package com.syd.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/24 下午5:52
 */
@Controller
public class JianDingPController {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    FormService formService;


    String taskId = "";

    /**
     * 任务处理
     * 鉴定管理的执行被指定的任务（任务处理）
     * @return
     */
    @RequestMapping("jianDingP/complateChuLiTask")
    @ResponseBody
    public String complateChuLiTask(){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId=task.getId();
        }
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),"测试任务处理数据");
        }
        try {
            taskService.complete(taskId,variables);
            return "任务分配已完成，任务Id为"+taskId+";变量："+variables;
        }catch (Exception e){
            return "完成任务时发生错误";
        }
    }


    /**
     * 确认签发
     * 鉴定人完成被指定的任务（确认签发）
     * @return
     */
    @RequestMapping("jianDingP/complateQianFaTask")
    @ResponseBody
    public String complateQianFaTask(){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId=task.getId();
        }
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),"测试确认签发数据");
        }
        try {
            taskService.complete(taskId,variables);
            return "确认签发任务已完成，任务Id为"+taskId+";变量："+variables;
        }catch (Exception e){
            return "完成任务时发生错误";
        }
    }

}
