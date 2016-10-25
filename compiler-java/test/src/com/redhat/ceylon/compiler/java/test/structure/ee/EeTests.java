package com.redhat.ceylon.compiler.java.test.structure.ee;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class EeTests extends CompilerTests {

    @Test
    public void testNoFinalMethods() {
        compareWithJavaSource("NoFinalMethods");
    }
    
    @Test
    public void testPublicImplicitCtor() {
        compareWithJavaSource("PublicImplicitCtor");
    }
    
    @Test
    public void testUncheckedLate() {
        compareWithJavaSource("UncheckedLate");
    }
    
}
