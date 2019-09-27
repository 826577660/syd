package com.syd.controller;

import com.syd.activiti.StartProcess;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
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
 * Created by ubuntu on 2019/9/24 下午4:49
 */
@Controller
public class ShangWuPController {
    @Autowired
    TaskService taskService;
    @Autowired
    FormService formService;

    String taskId="";

    /**
     * 商审
     * 商务员完成商审
     * @param condition 设置通过网关的条件(shangshen=商审通过，商审中止，退回计审)
     * @return
     */
    @RequestMapping("shangWuP/complateTask")
    @ResponseBody
    public String complateTask(String condition){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId = task.getId();
        }
        //获取表单属性 作为key value放入map中
        FormData data = formService.getTaskFormData(taskId);
        List<FormProperty> formProperties = data.getFormProperties();
        Map<String,Object> propMap = new HashMap<String,Object>();
        for (FormProperty formProperty:formProperties){
            propMap.put(formProperty.getId(),formProperty.getName());
        }
        try {
            variables.put("shangshen",condition);
            taskService.complete(taskId,variables);
            return "商审任务已完成，任务Id为"+taskId+";变量："+variables+";表单数据："+propMap;
        }catch (Exception e){
            return "完成任务时发生错误";
        }

    }
}
