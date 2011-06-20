package com.redhat.ceylon.compiler.test.statement;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StatementTest extends CompilerTest {
	
	//
	// Method attributes and variables
	
	@Test
	public void testMethodAttribute(){
		compareWithJavaSource("attribute/MethodAttribute");
	}
	
	@Test
	public void testMethodAttributeWithInitializer(){
		compareWithJavaSource("attribute/MethodAttributeWithInitializer");
	}

	@Test
	public void testMethodAttributeWithLateInitializer(){
	    compareWithJavaSource("attribute/MethodAttributeWithLateInitializer");
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
	public void testMethodIfElse(){
		compareWithJavaSource("conditional/MethodIfElse");
	}

	@Test
	public void testMethodIfElseIf(){
	    compareWithJavaSource("conditional/MethodIfElseIf");
	}

	@Test
	public void testMethodIfExists(){
		compareWithJavaSource("conditional/MethodIfExists");
	}

	@Test
	public void testMethodIfIs(){
		compareWithJavaSource("conditional/MethodIfIs");
	}

	@Test
	public void testMethodIfSatisfies(){
		compareWithJavaSource("conditional/MethodIfSatisfies");
	}

	@Test
	public void testMethodIfSatisfiesMultiple(){
		compareWithJavaSource("conditional/MethodIfSatisfiesMultiple");
	}

	@Test
	public void testMethodIfNonEmpty(){
		compareWithJavaSource("conditional/MethodIfNonEmpty");
	}

	//
	// switch / case
	
	@Test
	public void testMethodSwitch(){
		compareWithJavaSource("conditional/MethodSwitch");
	}

	@Test
	public void testMethodSwitchNB(){
		compareWithJavaSource("conditional/MethodSwitchNB");
	}

	@Test
	public void testMethodSwitchElse(){
		compareWithJavaSource("conditional/MethodSwitchElse");
	}

	@Test
	public void testMethodSwitchElseNB(){
		compareWithJavaSource("conditional/MethodSwitchElseNB");
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
	
	@Test
	public void testMethodForFail(){
		compareWithJavaSource("loop/MethodForFail");
	}
	
	//
	// [do] while
	
	@Test
	public void testMethodWhile(){
		compareWithJavaSource("loop/MethodWhile");
	}
	
	@Test
	public void testMethodDoWhile(){
		compareWithJavaSource("loop/MethodDoWhile");
	}
}
