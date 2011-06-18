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
}
