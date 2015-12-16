package com.ceylon.java9;

import java.lang.annotation.Annotation;

public class Test {
	public Test() throws Exception {
		System.err.println("Hello from Test.main");
		for(Annotation an : Test.class.getMethod("hashCode").getAnnotations())
			System.err.println(an);
	}
	public static void main(String[] args) throws Exception {
		new Test();
	}
}
