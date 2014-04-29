package com.ceylon.java8;

import java.util.List;

@TypeAnnotation
@TypeAnnotation
public class Test {
	public void methodWithParameterNames(String first, String second){}
	public void methodWithTypeAnnotation(List<@TypeAnnotation Test> l){
	}
}
