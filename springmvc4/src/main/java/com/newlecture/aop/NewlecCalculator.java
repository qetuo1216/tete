package com.newlecture.aop;

public class NewlecCalculator implements Calculator {

	@Override
	public int add(int x, int y) {
		//System.out.println("주업무 로직 시작전");
		int result = x + y;
		return result;
	}

	@Override
	public int sub(int x, int y) {
		int result = x - y;
		return result;
	}

	@Override
	public int multi(int x, int y) {
		int result = x * y;
		return result;
	}

	@Override
	public int div(int x, int y) {
		int result = x / y;
		return result;
	}

}
