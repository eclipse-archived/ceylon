package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class StaticTests extends CompilerTests {
    @Override
    protected String transformDestDir(String name) {
        return name + "-static";
    }
    
    @Test
    public void testStaticAttribute() {
        compareWithJavaSource("attribute/StaticAttribute");
    }
    
    @Test
    public void testStaticAttributeGeneric() {
        compareWithJavaSource("attribute/StaticAttributeGeneric");
    }
    
    @Test
    public void testStaticMethod() {
        compareWithJavaSource("method/StaticMethod");
    }
    
    @Test
    public void testStaticClass() {
        compareWithJavaSource("klass/StaticClass");
    }
    
    @Test
    public void testStaticMemberClass() {
        compareWithJavaSource("klass/StaticMemberClass");
    }
    
    @Test
    public void testStaticObject() {
        compareWithJavaSource("klass/StaticObject");
    }
    
    @Test
    public void testStaticAlias() {
        compareWithJavaSource("alias/StaticAlias");
    }
}
