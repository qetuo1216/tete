package com.newlecture.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Program {

	public static void main(String[] args) {
		
		Calculator origin = new NewlecCalculator();
		
		//������ ��� �־���(�Լ�)�� ���� �ȾƳ��� ��������,,
		//Proxy�� �����ؼ� ���� �־��� ������ �����ؾ��Ѵ�.
		//loader,              interfaces                        h
		//������ �ε�����, ������ �ش��ϴ� ������ �������̽�  ,�ڵ鷯(���������� ���� ��)
		Calculator cal = (Calculator) Proxy.newProxyInstance(
				NewlecCalculator.class.getClassLoader(), 
				new Class[] {Calculator.class}, 
				new InvocationHandler() {
			
					@Override					//Method method : ���� �����ϴ� �޼��带 ȣ���Ҷ� ���
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						System.out.println("����ó�� �������� : �־��� ȣ�� �Ǳ� ���̶�� �����");
						
						Object result = method.invoke(origin, args); //�Լ� ȣ��
						
						return result;
					}
				});
		
		int data = cal.add(3, 4);
		
		System.out.println(data);
	}

}
