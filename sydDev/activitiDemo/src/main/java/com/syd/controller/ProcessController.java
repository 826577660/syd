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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/25 下午3:17
 */
@Controller
public class ProcessController {
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

    /**
     * 根据指定人查询出该人的任务信息
     * @param assignee 代办人
     * @return 返回任务列表
     */
    @RequestMapping("process/getTaskInfo")
    @ResponseBody
    public List<Map<String,Object>> getTaskInfo(String assignee){//任务代办人----zhiFaP ,jianDingGuanLiP ,shangWuP ,jianDingP
        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Task> list =taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task:list){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("任务ID",task.getId());
            map.put("任务名称",task.getName());
            map.put("任务的创建时间",task.getCreateTime());
            map.put("任务办理人",task.getAssignee());
            map.put("流程实例ID",task.getProcessInstanceId());
            map.put("执行对象ID",task.getExecutionId());
            map.put("流程定义ID",task.getProcessDefinitionId());
            list1.add(map);
        }
        return list1;
    }

    /**
     * 根据任务id获取该任务的变量和信息
     * @param taskId 任务id
     * @return 返回表单字段和变量
     */
    @RequestMapping("process/getFormInfo")
    @ResponseBody
    public Map getFormInfo(String taskId){
        Map<String,Object> map = new HashMap<String,Object>();
        TaskFormData data=formService.getTaskFormData(taskId);
        List<FormProperty> properties=data.getFormProperties();
        Map<String,Object> propMap = new HashMap<String,Object>();
        for (FormProperty property:properties){
            propMap.put(property.getId(),property.getName());
        }
        map.put("表单数据",propMap);

        Map map1 = taskService.getVariables(taskId);
        map.put("变量",map1);
        return map;
    }

    /*List<Task> tasks =taskService.createTaskQuery().list();
            for (Task task:tasks){
                taskId = task.getId();
            }*/

    /**
     * 完成任务接口
     * @param taskId 任务id
     * @param mapData 表单变量参数
     * @return
     */
    @RequestMapping("process/complateTask")
    @ResponseBody
    public Map complateTask(@RequestParam String taskId,@RequestParam Map<String,Object> mapData){
        Map<String,Object> variables = new HashMap<String,Object>();
        System.out.println("canshu:"+mapData+taskId);
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            for (Map.Entry<String,Object> entry:mapData.entrySet() ){
                System.out.println("id:"+property.getId());
                if (property.getId().equals(entry.getKey())){
                    variables.put(property.getId(),entry.getValue());
                }else {
                    variables.put(entry.getKey(),entry.getValue());
                }
            }
        }
        System.out.println(variables);
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            taskService.complete(taskId,variables);
            map.put("taskId",taskId);
            map.put("message","用户任务已完成");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("message","完成任务时发生错误");
            return map;
        }

    }
}
