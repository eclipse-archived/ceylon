package com.redhat.ceylon.compiler.test.statement;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StatementTest extends CompilerTest {
	@Test
	public void testMethodAttribute(){
		// this one should fail
		compareWithJavaSource("attribute/MethodAttribute");
	}
	
	@Test
	public void testMethodAttributeWithInitializer(){
		// this one should fail
		compareWithJavaSource("attribute/MethodAttributeWithInitializer");
	}

	@Test
	public void testMethodVariable(){
		compareWithJavaSource("attribute/MethodVariable");
	}

	@Test
	public void testMethodVariableWithInitializer(){
		// this one should fail
		compareWithJavaSource("attribute/MethodVariableWithInitializer");
	}
}
