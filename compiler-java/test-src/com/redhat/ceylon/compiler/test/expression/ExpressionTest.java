package com.redhat.ceylon.compiler.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class ExpressionTest extends CompilerTest {
	
	// Attributes
	
    @Test
    public void testAtrToplevelAttributeAccess(){
        compareWithJavaSource("attribute/TopLevelAccess");
    }
    @Test
    public void testAtrToplevelAttributeAssign(){
        compareWithJavaSource("attribute/TopLevelAssign");
    }
    @Test
    public void testAtrInitializerParamAccess(){
        compareWithJavaSource("attribute/InitializerParamAccess");
    }
    @Test
    public void testAtrAttributeAccess(){
        compareWithJavaSource("attribute/AttributeAccess");
    }
    @Test
    public void testAtrAttributeAssign(){
        compareWithJavaSource("attribute/AttributeAssign");
    }
    @Test
    public void testAtrQualifiedAttributeAccess(){
        compareWithJavaSource("attribute/QualifiedAttributeAccess");
    }
    @Test
    public void testAtrQualifiedAttributeAssign(){
        compareWithJavaSource("attribute/QualifiedAttributeAssign");
    }
    @Test
    public void testAtrMethodParamAccess(){
        compareWithJavaSource("attribute/MethodParamAccess");
    }
    @Test
    public void testAtrMethodLocalAccess(){
        compareWithJavaSource("attribute/MethodLocalAccess");
    }
    @Test
    public void testAtrMethodLocalAssign(){
        compareWithJavaSource("attribute/MethodLocalAssign");
    }
    @Test
    public void testAtrInitializerParamAccessInInitializer(){
        compareWithJavaSource("attribute/InitializerParamAccessInInitializer");
    }
    
    //
    // Scope and hiding
    
    @Test
    public void testAtrInitializerParamHidingAttribute(){
        compareWithJavaSource("attribute/InitializerParamHidingAttribute");
    }
    @Test
    public void testAtrInitializerParamHidingGetter(){
        compareWithJavaSource("attribute/InitializerParamHidingGetter");
    }
    @Test
    public void testAtrMethodLocalHidingAttribute(){
        compareWithJavaSource("attribute/MethodLocalHidingAttribute");
    }
    @Test
    public void testAtrMethodParamHidingAttribute(){
        compareWithJavaSource("attribute/MethodParamHidingAttribute");
    }
    @Test
    public void testAtrAttributeHidingMethodParam(){
        compareWithJavaSource("attribute/AttributeHidingMethodParam");
    }


    // Literals
    
    @Test
    public void testLitStringLiteral(){
        compareWithJavaSource("literal/StringLiteral");
    }
    @Test
    public void testLitNumericLiteral(){
        compareWithJavaSource("literal/NumericLiteral");
    }
    
    // Method invocation
    
    @Test
    public void testInvMethodInvocation(){
        compareWithJavaSource("invoke/MethodInvocation");
    }

    @Test
    public void testInvToplevelMethodInvocation(){
        compareWithJavaSource("invoke/ToplevelMethodInvocation");
    }

    @Test
    public void testInvChainedInvocations(){
        compareWithJavaSource("invoke/ChainedInvocations");
    }

    @Test
    public void testInvNamedArgumentInvocation(){
        compareWithJavaSource("invoke/NamedArgumentInvocation");
    }

    // Self references
    
    @Test
    public void testSlfThisReference(){
        compareWithJavaSource("selfref/ThisReference");
    }
    @Test
    public void testSlfSuperReference(){
        compareWithJavaSource("selfref/SuperReference");
    }
    
    // Instantiation
    
    @Test
    public void testInsClassInstantiation(){
        compareWithJavaSource("instantiation/ClassInstantiation");
    }
    @Test
    public void testInsSequenceInstantiation(){
        compareWithJavaSource("instantiation/SequenceInstantiation");
    }
    @Test
    public void testInsEntryInstantiation(){
        compareWithJavaSource("instantiation/EntryInstantiation");
    }
    
    // Operators
    
    @Test
    public void testOprNumericOp(){
        compareWithJavaSource("operator/NumericOp");
    }
    @Test
    public void testOprLogialOp(){
        compareWithJavaSource("operator/LogicalOp");
    }
}
