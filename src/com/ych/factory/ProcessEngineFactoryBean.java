package com.ych.factory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import com.founder.fix.fixflow.core.ProcessEngine;
import com.founder.fix.fixflow.core.ProcessEngineManagement;
import com.founder.fix.fixflow.core.impl.ProcessEngineImpl;

/**
 * fixflowÒýÇæ spring¹¤³§
 * @author ych
 *
 */
public class ProcessEngineFactoryBean implements FactoryBean<ProcessEngine> ,DisposableBean{

	protected ApplicationContext applicationContext;
	protected ProcessEngineImpl processEngine;
	
	public ProcessEngine getObject() throws Exception {
		return ProcessEngineManagement.getDefaultProcessEngine();
	}

	public void destroy() throws Exception {
		if (processEngine != null) {
			processEngine.closeEngine();
		}
	}

	public Class<?> getObjectType() {
		return ProcessEngine.class;
	}
	
	public boolean isSingleton() {
		return true;
	}

}
