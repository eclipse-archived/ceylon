package com.redhat.ceylon.compiler.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class ExpressionTest extends CompilerTest {
	
	// Attributes
	
    @Test
    public void testToplevelAttributeAccess(){
        compareWithJavaSource("attribute/TopLevelAccess");
    }
    @Test
    public void testToplevelAttributeAssign(){
        compareWithJavaSource("attribute/TopLevelAssign");
    }
    @Test
    public void testInitializerParamAccess(){
        compareWithJavaSource("attribute/InitializerParamAccess");
    }
    @Test
    public void testAttributeAccess(){
        compareWithJavaSource("attribute/AttributeAccess");
    }
    @Test
    public void testAttributeAssign(){
        compareWithJavaSource("attribute/AttributeAssign");
    }
    @Test
    public void testMethodParamAccess(){
        compareWithJavaSource("attribute/MethodParamAccess");
    }
    @Test
    public void testMethodLocalAccess(){
        compareWithJavaSource("attribute/MethodLocalAccess");
    }
    @Test
    public void testMethodLocalAssign(){
        compareWithJavaSource("attribute/MethodLocalAssign");
    }
    @Test
    public void testInitializerParamAccessInInitializer(){
        compareWithJavaSource("attribute/InitializerParamAccessInInitializer");
    }
    
    //
    // Scope and hiding
    
    @Test
    public void testInitializerParamHidingAttribute(){
        compareWithJavaSource("attribute/InitializerParamHidingAttribute");
    }
    @Test
    public void testInitializerParamHidingVariable(){
        compareWithJavaSource("attribute/InitializerParamHidingVariable");
    }
    @Test
    public void testInitializerParamHidingGetter(){
        compareWithJavaSource("attribute/InitializerParamHidingGetter");
    }
    @Test
    public void testMethodLocalHidingAttribute(){
        compareWithJavaSource("attribute/MethodLocalHidingAttribute");
    }
    @Test
    public void testMethodParamHidingAttribute(){
        compareWithJavaSource("attribute/MethodParamHidingAttribute");
    }
    @Test
    public void testAttributeHidingMethodParam(){
        compareWithJavaSource("attribute/AttributeHidingMethodParam");
    }


    // Literals
    
    @Test
    public void testStringLiteral(){
        compareWithJavaSource("literal/StringLiteral");
    }
    @Test
    public void testNumericLiteral(){
        compareWithJavaSource("literal/NumericLiteral");
    }
    
    // Method invocation
    
    @Test
    public void testMethodInvocation(){
        compareWithJavaSource("invoke/MethodInvocation");
    }
    
    // Self references
    
    @Test
    public void testThisReference(){
        compareWithJavaSource("selfref/ThisReference");
    }
    @Test
    public void testSuperReference(){
        compareWithJavaSource("selfref/SuperReference");
    }
    
    // Instantiation
    
    @Test
    public void testClassInstantiation(){
        compareWithJavaSource("instantiation/ClassInstantiation");
    }
    @Test
    public void testSequenceInstantiation(){
        compareWithJavaSource("instantiation/SequenceInstantiation");
    }
    @Test
    public void testEntryInstantiation(){
        compareWithJavaSource("instantiation/EntryInstantiation");
    }
    
    // Operators
    
    @Test
    public void testNumericOp(){
        compareWithJavaSource("operator/NumericOp");
    }
}
