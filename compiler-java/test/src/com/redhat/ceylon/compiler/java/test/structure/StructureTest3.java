package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class StructureTest3 extends CompilerTest {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-3";
    }
    
    @Test
    public void testMcrInstantiatorDelegates(){
        compareWithJavaSource("mcr/InstantiatorDelegates");
    }
    
    //
    // Member Class Refinement
    
    // Default Member Class of Class
    @Test
    public void testMcrClassDefaultMemberClass(){
        compareWithJavaSource("mcr/ClassDefaultMemberClass");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithDefaultedParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithDefaultedParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithSequencedParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithSequencedParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithTypeParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithTypeParams");
    }
    
    @Test
    public void testMcrClassMemberClassesWithMemberSubclasses(){
        compareWithJavaSource("mcr/ClassMemberClassesWithMemberSubclasses");
    }
    
    // Default Member Class of Interface
    @Test
    public void testMcrInterfaceDefaultMemberClass(){
        compareWithJavaSource("mcr/InterfaceDefaultMemberClass");
    }
    
    @Test
    public void testMcrInterfaceMemberClassesWithMemberSubclasses(){
        compareWithJavaSource("mcr/InterfaceMemberClassesWithMemberSubclasses");
    }
    
    // Formal Member Class of Class
    @Test
    public void testMcrClassFormalMemberClass(){
        compareWithJavaSource("mcr/ClassFormalMemberClass");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassReference(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassReference");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassSpecifier(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassSpecifier");
    }
    
    // Formal Member Class of Interface
    @Test
    public void testMcrInterfaceFormalMemberClass_fail(){
        compareWithJavaSource("mcr/InterfaceFormalMemberClass");
    }
}