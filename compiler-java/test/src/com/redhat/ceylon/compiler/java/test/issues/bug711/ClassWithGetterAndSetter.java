package com.redhat.ceylon.compiler.java.test.issues.bug711;

public class ClassWithGetterAndSetter implements InterfaceWithGetter {

	protected String someString;

	public String getSomeString() {
		return someString;
	}
	
	public void setSomeString(String someString) {
		this.someString = someString;
	}
	
}
