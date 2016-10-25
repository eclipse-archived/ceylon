package com.redhat.ceylon.compiler.java.test.structure.ee;

import java.util.ArrayList;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class EeTests extends CompilerTests {

    @Override
    protected void compareWithJavaSource(String java, String... ceylon) {
        ArrayList<String> list = new ArrayList<String>(defaultOptions);
        list.add("-ee");
        compareWithJavaSource(list, java, ceylon);
    }
    
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
