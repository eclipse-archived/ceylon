package com.redhat.ceylon.compiler.test.issues;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class IssuesTest extends CompilerTest {
    
    @Override
    protected String getSourcePath() {
        return path;
    }
	
    @Test
    public void testBug111(){
        compareWithJavaSource("Bug111");
    }
    
    @Test
    public void testBug148(){
        compareWithJavaSource("bug148/Bug148.src", "bug148/Bug148.ceylon", "bug148/Bug148_fib.ceylon");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.test.issues.bug151", "Bug151.ceylon");
    }
}
