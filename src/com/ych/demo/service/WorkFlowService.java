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
 * fixflow ��������service
 * @author ych
 *
 */
@Service("workFlowService")
public class WorkFlowService {

	// �������
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected ProcessEngine processEngine;
	
	/**
	 * ����ӿ�Ӧ�ý��ղ�ѯ�����ͷ�ҳ�����ģ���ϸ�ο�bpmcenter�е�ʾ����ʱ���ϵ���򵥴�����
	 * @return
	 */
	public List<Map<String,Object>> getToDoTask(){
		List<Map<String,Object>> taskResult = new ArrayList<Map<String,Object>>();
		try{
			TaskQuery taskQuery = taskService.createTaskQuery();
			//��ѯadmin�Ĺ���Ͷ�ռ���񣬴˴�Ӧ�ô�session���õ�ǰ��¼�û�
			taskQuery.taskAssignee("1200119390");
			taskQuery.taskCandidateUser("1200119390");
			taskQuery.taskNotEnd();
			List<TaskInstance> tasks = taskQuery.list();
			
			for(TaskInstance task : tasks){
				taskResult.add(task.getPersistentState());
			}
		}finally{
			//�����õ�����service�ĵط����һ��Ҫ��������������������̴߳���
			processEngine.contextClose(true, false);
		}
		
		return taskResult;
	}
	
	/**
	 * ִ������
	 * �˴��߼����Ǻ�������ֻ��������ͨ��ť�������߼���bpmcenter��ʾ��
	 * @param parasMap
	 */
	public void executeCommand(Map<String,Object> parasMap){
		//����Ӧ��session��ȡ
		String userId = "1200119390";
		//����һ��Ҫд��Ҫ����ǰ�̸߳������ò����ˣ�bpmcenterʾ���У�����getProcessEngine(userId)�����ģ�����springû��ע�룬ֻ��ͨ�����ַ�ʽ����
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
			//���������ó־û�����  5.2��������bug,���github  issue#221
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
			//�����õ�����service�ĵط����һ��Ҫ��������������������̴߳���
			processEngine.contextClose(true, false);
		}
		
	}
	
	/**
	 * ��ȡ����toobar��Ϣ
	 * �������taskId����ȡ�����ϵĴ�������
	 * ����ͨ��processDefinitionKeyȡ��ʼ�ڵ��ϵ��������ã����ڿ�ʼ����
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
				throw new RuntimeException("��������");
			}
		}finally{
			//�����õ�����service�ĵط����һ��Ҫ��������������������̴߳���
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
