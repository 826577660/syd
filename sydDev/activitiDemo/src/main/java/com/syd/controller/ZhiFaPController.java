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
import java.util.Properties;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/23 下午4:25
 */
@Controller
public class ZhiFaPController {

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
     * 获取当前任务信息和表单数据
     * @param assignee 任务代办人----zhiFaP ,jianDingGuanLiP ,shangWuP ,jianDingP
     * @return
     */
    @RequestMapping("/getTaskInfo")
    @ResponseBody
    public Map getTaskInfo(String assignee){
        Map<String,Object> map = new HashMap<String,Object>();
        List<Task> list =taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task:list){
            taskId = task.getId();
            map.put("任务ID",task.getId());
            map.put("任务名称",task.getName());
            map.put("任务的创建时间",task.getCreateTime());
            map.put("任务办理人",task.getAssignee());
            map.put("流程实例ID",task.getProcessInstanceId());
            map.put("执行对象ID",task.getExecutionId());
            map.put("流程定义ID",task.getProcessDefinitionId());
        }

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

    /**
     * 打开或创建证据集
     * 执法人员打开或创建证据集，完成个人任务
     * @param condition 设置通过网关的条件(chuangjian=申请司法鉴定/删除)
     * @return
     */
    @RequestMapping("zhiFaP/complateTask")
    @ResponseBody
    public String complateTask(String condition){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> tasks =taskService.createTaskQuery().list();
        for (Task task:tasks){
            taskId = task.getId();
        }
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),property.getName());
        }
        variables.put("chuangjian",condition);
        try {
            taskService.complete(taskId,variables);
            return "任务Id为"+taskId+"用户任务完成！变量为："+variables;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "完成任务时发生错误";
    }


    /**
     * 删除流程
     * 当执法人员点击删除完成此方法
     * @return
     */
    @RequestMapping("zhiFaP/delete")
    @ResponseBody
    public String deleteTask(){
        String processId = startProcess.shutdown();
        return "流程Id为"+processId+"de流程已被删除";
    }

    /**
     * 申请司法鉴定
     * 执法人员完成申请司法鉴定的个人任务
     * @return 返回个人设置的流程变量
     */
    @RequestMapping("zhiFaP/shenqingjianding")
    @ResponseBody
    public String complateTask(){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task:list){
            taskId = task.getId();
        }

       TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),"测试数据");
        }
        try {
            taskService.complete(taskId,variables);
            //查询变量以及上个任务的变量
            /*Map<String,Object> map = taskService.getVariables(taskId);
            variables.put("变量",map);*/
            return "申请司法鉴定任务已完成，Id为"+taskId+"用户任务完成！变量为："+variables;
        }catch (Exception e){
            e.printStackTrace();
            return "完成任务时发生错误";
        }

    }

    /**
     * 补充
     * 执法人员完成补充任务
     * @param condition 设置通过网关的条件（buchong=重新申请鉴定/删除）
     * @return
     */
    @RequestMapping("zhiFaP/buChong")
    @ResponseBody
    public String complateBuChongTask(String condition){
        Map<String,Object> variables = new HashMap<String,Object>();
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task:list){
            taskId = task.getId();
        }
        TaskFormData data = formService.getTaskFormData(taskId);
        List<FormProperty> properties = data.getFormProperties();
        for (FormProperty property:properties){
            variables.put(property.getId(),"测试已补充数据");
        }
        variables.put("buchong",condition);
        taskService.complete(taskId,variables);

        return "补充任务已完成，任务Id为"+taskId+";设置的变量："+variables;
    }


    /**
     * 评价
     * 执法人员完成被指定的任务（评价）
     * @return
     */
    @RequestMapping("zhiFaP/complatePingJiaTask")
    @ResponseBody
    public String complatePingJiaTask(){
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
        try {
            taskService.complete(taskId,variables);
            return "评价任务已完成，任务Id为"+taskId+";变量："+variables;
        }catch (Exception e){
            return "完成任务时发生错误";
        }
    }
}
