package com.newlecture.aop.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;

public class LogHandler implements MethodInterceptor { //around advice

	@Override
	public Object invoke(MethodInvocation method) throws Throwable {
		
		StopWatch watch = new StopWatch();
		
		System.out.println("������ ���� ��������");
		
		watch.start();
		Object result = method.proceed();
		watch.stop();
		
		long du = watch.getTotalTimeMillis();
		
		System.out.println(method.getMethod().getName() + "() ȣ�⿡ " + du + "�и��ʰ� �ɷȽ��ϴ�.");
		
		return result;
	}

}
