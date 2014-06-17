package com.ych.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.founder.fix.fixflow.core.impl.identity.Authentication;
import com.ych.demo.service.WorkFlowService;

/**
 * fixflow的拦截器，拦截所有用到ProcessEngine的方法
 * 1.方法执行前，设置当前引擎的操作人
 * 2.方法执行后，清空线程副本
 * @author ych
 *
 */
public class FixflowInterceptor implements AfterReturningAdvice,
		MethodBeforeAdvice {


	@Override
	public void before(Method arg0, Object[] arg1, Object arg2)
			throws Throwable {
		//设置当前操作人，这里应该从当前session读取，在每次执行的时候都要设置
		//如果有多个service用到fixflow引擎，则应该将service抽象成接口进行判断，这里只有一个，所以省事了
		if(arg2 instanceof WorkFlowService){
			Authentication.setAuthenticatedUserId("1200119390");
		}
	}

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		//如果有多个service用到fixflow引擎，则应该将service抽象成接口进行判断，这里只有一个，所以省事了
		if(arg3 instanceof WorkFlowService){
			WorkFlowService serivce = (WorkFlowService)arg3;
			serivce.closeEngine();
		}
	}

}
