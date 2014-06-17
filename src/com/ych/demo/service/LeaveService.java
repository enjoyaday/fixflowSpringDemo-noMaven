package com.ych.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.fix.fixflow.core.impl.util.StringUtil;
import com.ych.demo.dao.LeaveDao;
import com.ych.demo.model.LeaveModel;

/**
 * 请假service处理
 * @author ych
 */
@Service("leaveService")
public class LeaveService {

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private LeaveDao leaveDao;
	
	/**
	 * 请假处理，包括请假开始和审批
	 * 如果taskId为空，则保存业务数据，进行发起操作，否则，不处理业务数据，进行审批操作
	 * @param params
	 */
	public void saveLeave(Map<String,Object> params){
		
		String qjbh = params.get("qjbh").toString();
		String nextAssgine = StringUtil.getString(params.get("nextAssgine"));
		
		Object taskId = params.get("taskId");
		//不存在taskId,表示第一次启动流程，需要保存业务数据
		if(taskId == null || taskId.equals("")){
			String qjr = params.get("qjr").toString();
			String qjsj = params.get("qjsj").toString();
			
			LeaveModel leave = new LeaveModel();
			leave.setQjbh(qjbh);
			leave.setQjr(qjr);
			leave.setQjsj(qjsj);
			
			leaveDao.saveLeave(leave);
		}
		
		//将主键作为关连建给流程引擎
		params.put("bizKey", qjbh);
		//给流程设置变量，nextAssignee在流程定义中被设置为审批节点的处理人，详见流程定义
		
		if(StringUtil.isNotEmpty(nextAssgine)){
			Map<String,Object> taskVariable = new HashMap<String,Object>();
			taskVariable.put("nextAssignee", nextAssgine);
			params.put("taskVariable", taskVariable);
		}
		
		workFlowService.executeCommand(params);
		
	}
	
	/**
	 * 查询请假信息，用于任务处理界面展现
	 * @param qjbh
	 * @return
	 */
	public Map<String,Object> selectLeaveById(String qjbh){
		return leaveDao.selectLeaveById(qjbh);
	}

}
