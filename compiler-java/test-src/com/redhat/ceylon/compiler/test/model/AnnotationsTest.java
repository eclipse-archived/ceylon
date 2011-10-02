package com.redhat.ceylon.compiler.test.model;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class AnnotationsTest extends CompilerTest {
    @Test
    public void testUnionTypeInfo(){
        compareWithJavaSource("annotations/UnionTypeInfo");
    }
    @Test
    public void testClass(){
        compareWithJavaSource("annotations/Klass");
    }
    @Test
    public void testMethod(){
        compareWithJavaSource("annotations/method");
    }
    @Test
    public void testAttribute(){
        compareWithJavaSource("annotations/attribute");
    }
}
