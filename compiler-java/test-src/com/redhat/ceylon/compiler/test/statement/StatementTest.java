package com.redhat.ceylon.compiler.test.statement;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StatementTest extends CompilerTest {
	
	//
	// Method attributes and variables
	
	@Test
	public void testMethodAttribute(){
		// this one should fail
		compareWithJavaSource("attribute/MethodAttribute");
	}
	
	@Test
	public void testMethodAttributeWithInitializer(){
		compareWithJavaSource("attribute/MethodAttributeWithInitializer");
	}

	@Test
	public void testMethodVariable(){
		compareWithJavaSource("attribute/MethodVariable");
	}

	@Test
	public void testMethodVariableWithInitializer(){
		compareWithJavaSource("attribute/MethodVariableWithInitializer");
	}

	//
	// if/else

	@Test
	public void testInitializerIf(){
		compareWithJavaSource("conditional/InitializerIf");
	}

	@Test
	public void testInitializerIfElse(){
		compareWithJavaSource("conditional/InitializerIfElse");
	}

	@Test
	public void testInitializerIfElseIf(){
		compareWithJavaSource("conditional/InitializerIfElseIf");
	}

	@Test
	public void testMethodIf(){
		compareWithJavaSource("conditional/MethodIf");
	}

	@Test
	public void testMethodElse(){
		compareWithJavaSource("conditional/MethodIfElse");
	}
	
	//
	// for

	@Test
	public void testMethodForRange(){
		compareWithJavaSource("loop/MethodForRange");
	}
	
	@Test
	public void testMethodForIterator(){
		compareWithJavaSource("loop/MethodForIterator");
	}
	
	@Test
	public void testMethodForDoubleIterator(){
		compareWithJavaSource("loop/MethodForDoubleIterator");
	}
}
