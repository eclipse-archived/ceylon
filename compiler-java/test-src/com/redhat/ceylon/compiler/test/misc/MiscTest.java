package com.redhat.ceylon.compiler.test.misc;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class MiscTest extends CompilerTest {
	@Test
	public void testHelloWorld(){
		compareWithJavaSource("helloworld/helloworld");
	}
	
	@Test
	public void runHelloWorld() throws Exception{
		compileAndRun("helloworld/helloworld.ceylon", "com.redhat.ceylon.compiler.test.misc.helloworld.helloworld");
	}

    @Test
    public void testDefaultPackage(){
        compareWithJavaSource("defaultPackage", path);
    }
}
