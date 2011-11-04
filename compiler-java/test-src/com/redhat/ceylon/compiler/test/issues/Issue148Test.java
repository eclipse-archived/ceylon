package com.redhat.ceylon.compiler.test.issues;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

/**
 * Special issue with source path mimicking a one-package qualified name 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Issue148Test extends CompilerTest {
    
    @Override
    protected String getSourcePath() {
        return path;
    }
	
    @Test
    public void testBug148(){
        compareWithJavaSource("bug148/Bug148.src", "bug148/Bug148.ceylon");
        compareWithJavaSource("bug148_2/Bug148_2.src", "bug148_2/Bug148_2.ceylon");
        compareWithJavaSource("bug148_3/Bug148_3.src", "bug148_3/Bug148_3.ceylon");
    }
    
}
