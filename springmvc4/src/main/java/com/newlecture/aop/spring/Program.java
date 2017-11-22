package com.newlecture.aop.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Program {

	public static void main(String[] args) {
		/*
		Calculator origin = new NewlecCalculator();
		
		//일일이 모든 주업무(함수)에 직접 꽂아넣지 않으려면,,
		//Proxy를 생성해서 실제 주업무 로직을 위임해야한다.
		//loader,              interfaces                        h
		//누구를 로드할지, 원본에 해당하는 동일한 인터페이스  ,핸들러(보조업무가 들어가는 곳)
		Calculator cal = (Calculator) Proxy.newProxyInstance(
				NewlecCalculator.class.getClassLoader(), 
				new Class[] {Calculator.class}, 
				new InvocationHandler() {
			
					@Override					//Method method : 실제 존재하는 메서드를 호출할때 사용
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						System.out.println("사전처리 보조업무 : 주업무 호출 되기 전이라면 여기야");
						
						Object result = method.invoke(origin, args);
						
						return result;
					}
				});*/
		
		ApplicationContext context = new ClassPathXmlApplicationContext("com/newlecture/aop/spring/aop-context.xml");
		
		Calculator cal = (Calculator) context.getBean("cal");
		
		int data = cal.add(3, 4);
		data = cal.div(6, 1);
		
		System.out.println(data);
	}

}
