package com.ych.demo.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ych.demo.service.LeaveService;
import com.ych.demo.service.WorkFlowService;

@Controller
public class LeaveController {

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private LeaveService leaveService;
	
	/**
	 * 加载请假开始界面，加载出相应的toolbar
	 * @param request
	 * @return
	 */
	@RequestMapping("startTask")
	public ModelAndView startTask(HttpServletRequest request){
		String processKey = request.getParameter("processDefinitionKey");
		Map<String,String> params = new HashMap<String, String>();
		params.put("processDefinitionKey", processKey);
		List<Map<String,Object>> toolbarInfo = workFlowService.getToolbarInfo(params);
		ModelAndView modelView = new ModelAndView("fixflow/startTask");
		modelView.addObject("commandList", toolbarInfo);
		return modelView;
	}
	
	/**
	 * 完成请假动作，包括发起和审批
	 * startTask和doTask中所有的命令均是此处处理
	 * @param request
	 * @return
	 */
	@RequestMapping("completeTask")
	public ModelAndView completeLeave(HttpServletRequest request){
		Map<String,Object> params = getRequestParams(request);
		leaveService.saveLeave(params);
		ModelAndView modelView = new ModelAndView("redirect:toDoTask.action");
		return modelView;
	}
	
	/**
	 * 加载任务处理界面的toolbar信息
	 * @param request
	 * @return
	 */
	@RequestMapping("doTask")
	public ModelAndView doTask(HttpServletRequest request){
		String taskId = request.getParameter("taskId");
		String bizKey = request.getParameter("bizKey");
		Map<String,Object> leave = leaveService.selectLeaveById(bizKey);
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("taskId", taskId);
		List<Map<String,Object>> toolbarInfo = workFlowService.getToolbarInfo(params);
		ModelAndView modelView = new ModelAndView("fixflow/doTask");
		modelView.addObject("commandList", toolbarInfo);
		modelView.addObject("leave", leave);
		return modelView;
	}
	
	
	/**
	 * http request 请求参数获取
	 * 
	 * @param request
	 *            http 请求
	 * @return 返回获取的http请求参数
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getRequestParams(HttpServletRequest request) {
		// 请求参数
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.putAll(request.getParameterMap());
		try {
			Enumeration<String> enumeration = null;
				// 获取parmeter中参数
			enumeration = request.getParameterNames();
			if (null != enumeration) {
				String key = null;
				String value = null;
				while (enumeration.hasMoreElements()) {
					key = enumeration.nextElement();
					value = request.getParameter(key);
					requestParams.put(key, new String(value.getBytes("ISO8859-1"), "utf-8"));
				}
			}
			// 获取attribute中参数
			enumeration = request.getAttributeNames();
			if (null != enumeration) {
				String key = null;
				while (enumeration.hasMoreElements()) {
					key = enumeration.nextElement();
					requestParams.put(key, request.getAttribute(key));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return requestParams;
	}
	
	
}
