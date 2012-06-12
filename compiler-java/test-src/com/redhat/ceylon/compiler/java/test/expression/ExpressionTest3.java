package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ExpressionTest3 extends CompilerTest {

    @Test
    public void testComprehensions1() {
        compareWithJavaSource("comprehensions/comp1");
    }
}
