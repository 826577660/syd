package com.syd.controller;

import com.syd.activiti.StartProcess;
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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/24 上午9:15
 */
@Controller
public class JianDingGuanLiPController {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    FormService formService;

    @Resource
    StartProcess startProcess;

    String taskId="";

    /**
     * 计审
     * 鉴定管理员完成计审
     * @param condition 设置通过网关的条件（计审通过，计审中止，退回司鉴）
     * @return
     */
    @RequestMapping("JianDingGuanLiP/complateTask")
    @ResponseBody
    public String complateJiShenTask(String condition){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId=task.getId();
        }
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),property.getName());
        }
        variables.put("jishen",condition);
        try {
            taskService.complete(taskId,variables);
            return "计审任务已完成，任务Id为"+taskId+";变量："+variables;
        }catch (Exception e){
            return "完成任务时发生错误";
        }
    }

    /**
     * 任务分配
     *鉴定管理员完成被指定的任务
     * @param condition 设置通过网关的条件（fenpei=确认签发，监管中止）
     * @return
     */
    @RequestMapping("JianDingGuanLiP/complateTaskFenPei")
    @ResponseBody
    public String complateTaskFenPei(String condition){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId=task.getId();
        }

        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        Map<String,Object> map = new HashMap<String,Object>();
        for (FormProperty property:properties){
            map.put(property.getId(),property.getName());
        }

        try {
            variables.put("fenpei",condition);
            taskService.complete(taskId,variables);
            return "任务分配已完成，任务Id为"+taskId+";变量："+variables+";表单数据："+map;
        }catch (Exception e){
            e.printStackTrace();
            return "完成任务时发生错误";
        }
    }
}
