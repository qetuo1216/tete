package com.newlecture.aop.spring;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;

public class AfterHandler implements AfterReturningAdvice{

	private Log log = LogFactory.getLog(this.getClass()); //로그 출력
	
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		
		//System.out.println("hhihihi");
		System.out.println(method.getName() + "() 호출 후 반환 된 값 : " + returnValue);
	} //after advice

}
