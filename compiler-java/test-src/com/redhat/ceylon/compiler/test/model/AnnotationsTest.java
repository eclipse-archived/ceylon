package com.redhat.ceylon.compiler.test.model;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class AnnotationsTest extends CompilerTest {
    @Test
    public void testUnionTypeInfo(){
        compareWithJavaSource("annotations/UnionTypeInfo");
    }
}
