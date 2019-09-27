package com.syd.controller;

import com.syd.activiti.StartProcess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Description: activitiDemo
 * Created by ubuntu on 2019/9/23 下午4:26
 */
@Controller
public class InitController {
    @Resource
    private StartProcess startProcess;

    /*
    启动流程
     */
    @RequestMapping("/startProcess")
    @ResponseBody
    public String startProcess(){
        String str = startProcess.start();
        return "流程Id为"+str+"的流程启动成功";
    }
    /*
    关闭流程
     */
    @RequestMapping("/shutDown")
    @ResponseBody
    public String shutDown(){
        String str  = startProcess.shutdown();
        return "流程Id为"+str+"的流程结束";
    }
    /*
    创建用户和组
     */
    @RequestMapping("/saveUserAndGroup")
    @ResponseBody
    public String saveUserAndGroup(){
        startProcess.saveUserAndGroup();
        return "创建用户和组成功";
    }
}
