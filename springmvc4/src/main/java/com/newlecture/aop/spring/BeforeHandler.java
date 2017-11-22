package com.newlecture.aop.spring;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class BeforeHandler implements MethodBeforeAdvice {// before advice

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		
		System.out.println("before handler");
		
		//method.invoke(obj, args);
	}

}
