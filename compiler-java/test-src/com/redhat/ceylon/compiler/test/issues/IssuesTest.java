package com.redhat.ceylon.compiler.test.issues;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class IssuesTest extends CompilerTest {
	
    @Test
    public void testBug111(){
        compareWithJavaSource("Bug111");
    }
    
    @Test
    public void testBug148(){
        compareWithJavaSource("Bug148.src", "Bug148.ceylon", "Bug148_fib.ceylon");
    }
}
