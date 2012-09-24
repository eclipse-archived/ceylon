package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ExpressionTest3 extends CompilerTest {

    @Test
    public void testComprehensions1() {
        compareWithJavaSource("comprehensions/comp1");
    }

    @Test
    public void testComprehensionForForIf() {
        compareWithJavaSource("comprehensions/for_for_if");
    }

    @Test
    public void testComprehensionForIfFor() {
        compareWithJavaSource("comprehensions/for_if_for");
    }

    @Test
    public void testComprehensionForIfIf() {
        compareWithJavaSource("comprehensions/for_if_if");
    }

    @Test
    public void testComprehensionIs() {
        compareWithJavaSource("comprehensions/is_cond");
    }

    @Test
    public void testComprehensionNonempty() {
        compareWithJavaSource("comprehensions/nonempty_cond");
    }

    @Test
    public void testComprehensionExists() {
        compareWithJavaSource("comprehensions/exists_cond");
    }

}
