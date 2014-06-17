package com.ych.demo.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ych.demo.service.WorkFlowService;

/**
 * 工作流任务中心action，包括待办，流程追踪，处理过程等与业务无关的操作。
 * @author ych
 *
 */
@Controller
public class WorkFlowController {

	@Resource(name = "workFlowService")
	private WorkFlowService workFlowService;
	
	
	@RequestMapping("toDoTask")
	public ModelAndView toDoTask(){
		
		ModelAndView modelAndView = new ModelAndView("/fixflow/toDoTask");
		List<Map<String,Object>> taskList = workFlowService.getToDoTask();
		modelAndView.addObject("taskList", taskList);
		return modelAndView;
	}
}
