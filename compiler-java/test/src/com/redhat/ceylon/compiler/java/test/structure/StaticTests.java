package com.redhat.ceylon.compiler.java.test.structure;

import java.lang.reflect.Constructor;
import java.util.Arrays;

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
    
    @Test
    public void testStaticLate() {
        compareWithJavaSource("attribute/StaticLate");
    }
    @Test
    public void testStatik() {
        compileAndReflect("klass/Statik.ceylon", "com.redhat.ceylon.compiler.java.test.structure.klass.StaticMembers", new ReflectionCallback() {
            
            @Override
            public void reflect(Class c) {
                for (Class<?> member : c.getClasses()) {
                    System.out.println(member);
                    for (Constructor<?> ctor : member.getDeclaredConstructors()) {
                        System.out.println(Arrays.toString(ctor.getParameterTypes()));
                        System.out.println(Arrays.toString(ctor.getParameters()));
                    }
                }
            }
        });
    }
}
