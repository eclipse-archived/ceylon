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
		compileAndRun("com.redhat.ceylon.compiler.test.misc.helloworld.helloworld", "helloworld/helloworld.ceylon");
	}

    @Test
    public void testDefaultPackage(){
        compareWithJavaSource("defaultPackage", path);
    }

    @Test
    public void testCompileTwoClasses() throws Exception{
        compileAndRun("com.redhat.ceylon.compiler.test.misc.twoclasses.main", "twoclasses/One.ceylon", "twoclasses/Two.ceylon", "twoclasses/main.ceylon");
    }
}
