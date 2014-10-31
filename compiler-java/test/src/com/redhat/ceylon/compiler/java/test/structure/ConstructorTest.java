package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ConstructorTest extends CompilerTest {
    @Override
    protected String transformDestDir(String name) {
        return name + "-4";
    }
    // Tests for constructors
    @Test
    public void testCtorNullaryCtor(){
        compareWithJavaSource("constructor/NullaryCtor");
        compareWithJavaSource("constructor/NullaryCtorUse");
    }
    @Test
    public void testCtorUnaryCtor(){
        compareWithJavaSource("constructor/UnaryCtor");
        compareWithJavaSource("constructor/UnaryCtorUse");
    }
    @Test
    public void testCtorDefaultedParameterCtor(){
        compareWithJavaSource("constructor/DefaultedParameterCtor");
    }
    @Test
    public void testCtorSequencedParameterCtor(){
        compareWithJavaSource("constructor/SequencedParameterCtor");
    }
    @Test
    public void testCtorCaptureInit(){
        compareWithJavaSource("constructor/CtorCaptureInit");
    }
    @Test
    public void testCtorGenericClass(){
        compareWithJavaSource("constructor/CtorGenericClass");
    }
    @Test
    public void testCtorChaining(){
        compareWithJavaSource("constructor/CtorChaining");
    }
    /*
    @Test
    public void testCtorClassMemberClassCtor(){
        compareWithJavaSource("constructor/ClassMemberClassCtor");
    }
    @Test
    public void testCtorInterfaceMemberClassCtor(){
        compareWithJavaSource("constructor/InterfaceMemberClassCtor");
    }
    @Test
    public void testCtorClassRefinedMemberClassCtor(){
        compareWithJavaSource("constructor/ClassRefinedMemberClassCtor");
    }
    @Test
    public void testCtorInterfaceRefinedMemberClassCtor(){
        compareWithJavaSource("constructor/InterfaceRefinedMemberClassCtor");
    }
    */
}
