package com.redhat.ceylon.compiler.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class ExpressionTest extends CompilerTest {
    @Test
    public void testToplevelAttributeAccess(){
        compareWithJavaSource("attribute/TopLevelAccess");
    }
    @Test
    public void testToplevelAttributeAssign(){
        compareWithJavaSource("attribute/TopLevelAssign");
    }
    @Test
    public void testInitializerParamAccess(){
        compareWithJavaSource("attribute/InitializerParamAccess");
    }
    @Test
    public void testAttributeAccess(){
        compareWithJavaSource("attribute/AttributeAccess");
    }
    @Test
    public void testAttributeAssign(){
        compareWithJavaSource("attribute/AttributeAssign");
    }
    @Test
    public void testMethodParamAccess(){
        compareWithJavaSource("attribute/MethodParamAccess");
    }
    @Test
    public void testMethodLocalAccess(){
        compareWithJavaSource("attribute/MethodLocalAccess");
    }
    @Test
    public void testMethodLocalAssign(){
        compareWithJavaSource("attribute/MethodLocalAssign");
    }
}
