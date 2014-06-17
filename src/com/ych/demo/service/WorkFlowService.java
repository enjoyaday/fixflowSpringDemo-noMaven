package com.ych.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.fix.fixflow.core.ProcessEngine;
import com.founder.fix.fixflow.core.TaskService;
import com.founder.fix.fixflow.core.impl.bpmn.behavior.TaskCommandInst;
import com.founder.fix.fixflow.core.impl.command.ExpandTaskCommand;
import com.founder.fix.fixflow.core.impl.identity.Authentication;
import com.founder.fix.fixflow.core.impl.util.StringUtil;
import com.founder.fix.fixflow.core.task.TaskInstance;
import com.founder.fix.fixflow.core.task.TaskQuery;

/**
 * fixflow 任务中心service
 * @author ych
 *
 */
@Service("workFlowService")
public class WorkFlowService {

	// 任务服务
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected ProcessEngine processEngine;
	
	/**
	 * 这里接口应该接收查询参数和分页参数的，详细参考bpmcenter中的示例，时间关系，简单处理了
	 * @return
	 */
	public List<Map<String,Object>> getToDoTask(){
		List<Map<String,Object>> taskResult = new ArrayList<Map<String,Object>>();
		try{
			TaskQuery taskQuery = taskService.createTaskQuery();
			//查询admin的共享和独占任务，此处应该从session里拿当前登录用户
			taskQuery.taskAssignee("1200119390");
			taskQuery.taskCandidateUser("1200119390");
			taskQuery.taskNotEnd();
			List<TaskInstance> tasks = taskQuery.list();
			
			for(TaskInstance task : tasks){
				taskResult.add(task.getPersistentState());
			}
		}finally{
			//所有用到引擎service的地方最后一定要做清理操作，否则会出现线程错乱
			processEngine.contextClose(true, false);
		}
		
		return taskResult;
	}
	
	/**
	 * 执行命令
	 * 此处逻辑不是很完整，只处理了普通按钮，完整逻辑见bpmcenter的示例
	 * @param parasMap
	 */
	public void executeCommand(Map<String,Object> parasMap){
		//这里应从session读取
		String userId = "1200119390";
		//这里一定要写，要给当前线程副本设置操作人，bpmcenter示例中，是在getProcessEngine(userId)里做的，这里spring没有注入，只能通过此种方式设置
		Authentication.setAuthenticatedUserId(userId);
		
		String taskId = StringUtil.getString(parasMap.get("taskId"));
		String commandType = StringUtil.getString(parasMap.get("commandType"));
		String commandId = StringUtil.getString(parasMap.get("commandId"));
		String processDefinitionKey = StringUtil.getString(parasMap.get("processDefinitionKey"));
		String businessKey = StringUtil.getString(parasMap.get("bizKey"));
		String taskComment = StringUtil.getString(parasMap.get("_taskComment"));
		
		@SuppressWarnings("unchecked")
		Map<String,Object> taskVariable = (Map<String, Object>) parasMap.get("taskVariable");
		

		ExpandTaskCommand expandTaskCommand = new ExpandTaskCommand();
		
		expandTaskCommand.setCommandType(commandType);
		expandTaskCommand.setInitiator(userId);
		expandTaskCommand.setUserCommandId(commandId);
		expandTaskCommand.setTaskComment(taskComment);
		
		if(taskVariable != null){
			//给流程设置持久化变量  5.2基础班有bug,详见github  issue#221
			expandTaskCommand.setVariables(taskVariable);
		}

		if (StringUtil.isNotEmpty(taskId)) {
			expandTaskCommand.setTaskId(taskId);
		} else {
			expandTaskCommand.setBusinessKey(businessKey);
			expandTaskCommand.setProcessDefinitionKey(processDefinitionKey);
		}
		
		try{
			taskService.expandTaskComplete(expandTaskCommand, null);
		}finally{
			//所有用到引擎service的地方最后一定要做清理操作，否则会出现线程错乱
			processEngine.contextClose(true, false);
		}
		
	}
	
	/**
	 * 获取流程toobar信息
	 * 如果存在taskId，则取任务上的处理命令
	 * 否则通过processDefinitionKey取开始节点上的命令配置，用于开始流程
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getToolbarInfo(Map<String,String> params){
		List<Map<String,Object>> toolbarInfo = new ArrayList<Map<String,Object>>();
		String processKey = params.get("processDefinitionKey");
		String taskId = params.get("taskId");
		List<TaskCommandInst> taskCommands = null;
		try{
			if(taskId != null){
				taskCommands = taskService.GetTaskCommandByTaskId(taskId, false);
			}
			else if(processKey != null){
				taskCommands = taskService.getSubTaskTaskCommandByKey(processKey);
			}else{
				throw new RuntimeException("参数错误");
			}
		}finally{
			//所有用到引擎service的地方最后一定要做清理操作，否则会出现线程错乱
			processEngine.contextClose(true, false);
		}
		
		if(taskCommands != null){
			for(TaskCommandInst taskCommand :taskCommands){
				Map<String,Object> commandMap = taskCommand.getPersistentState();
				toolbarInfo.add(commandMap);
			}
		}
		return toolbarInfo;
	}

}
