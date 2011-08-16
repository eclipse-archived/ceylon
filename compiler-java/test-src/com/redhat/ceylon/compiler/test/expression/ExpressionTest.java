package com.redhat.ceylon.compiler.test.expression;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class ExpressionTest extends CompilerTest {
	
	// Attributes
	
    @Test
    public void testAtrAttributeAccess(){
        compareWithJavaSource("attribute/AttributeAccess");
    }
    @Test
    public void testAtrAttributeAssign(){
        compareWithJavaSource("attribute/AttributeAssign");
    }
    @Test
    public void testAtrAttributeHidingMethodParam(){
        compareWithJavaSource("attribute/AttributeHidingMethodParam");
    }
    @Test
    public void testAtrIndirectQualifiedAttributeAccess(){
        compareWithJavaSource("attribute/IndirectQualifiedAttributeAccess");
    }
    @Test
    public void testAtrInitializerParamAccess(){
        compareWithJavaSource("attribute/InitializerParamAccess");
    }
    @Test
    public void testAtrInitializerParamAccessInInitializer(){
        compareWithJavaSource("attribute/InitializerParamAccessInInitializer");
    }
    @Test
    public void testAtrInitializerParamHidingAttribute(){
        compareWithJavaSource("attribute/InitializerParamHidingAttribute");
    }
    @Test
    public void testAtrInitializerParamHidingGetter(){
        compareWithJavaSource("attribute/InitializerParamHidingGetter");
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
    public void testAtrMethodLocalHidingAttribute(){
        compareWithJavaSource("attribute/MethodLocalHidingAttribute");
    }
    @Test
    public void testAtrMethodParamAccess(){
        compareWithJavaSource("attribute/MethodParamAccess");
    }
    @Test
    public void testAtrMethodParamHidingAttribute(){
        compareWithJavaSource("attribute/MethodParamHidingAttribute");
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
    public void testAtrToplevelAccess(){
        compareWithJavaSource("attribute/TopLevelAccess");
    }
    @Test
    public void testAtrToplevelAssign(){
        compareWithJavaSource("attribute/TopLevelAssign");
    }

    // Boxing and unboxing
    
    @Test
    public void testBoxBooleanBoxing(){
        compareWithJavaSource("boxing/BooleanBoxing");
    }
    @Test
    public void testBoxIntegerBoxing(){
        compareWithJavaSource("boxing/IntegerBoxing");
    }
    @Test
    public void testBoxStringBoxing(){
        compareWithJavaSource("boxing/StringBoxing");
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
    public void testInvChainedInvocations(){
        compareWithJavaSource("invoke/ChainedInvocations");
    }

    @Test
    public void testInvGenericMethodInvocation(){
        compareWithJavaSource("invoke/GenericMethodInvocation");
    }

    @Test
    public void testInvInvocationErasure(){
        compareWithJavaSource("invoke/InvocationErasure");
    }
    
    @Test
    public void testInvMethodInvocation(){
        compareWithJavaSource("invoke/MethodInvocation");
    }

    @Test
    @Ignore("Not for needed for M1")
    public void testInvNamedArgumentGetterInvocation(){
        compareWithJavaSource("invoke/NamedArgumentGetterInvocation");
    }

    @Test
    public void testInvNamedArgumentInvocation(){
        compareWithJavaSource("invoke/NamedArgumentInvocation");
    }
    
    @Test
    public void testInvNamedArgumentInvocationInit(){
        compareWithJavaSource("invoke/NamedArgumentInvocationInit");
    }
    
    @Test
    public void testInvNamedArgumentInvocationTopLevel(){
        compareWithJavaSource("invoke/NamedArgumentInvocationTopLevel");
    }
    
    @Test
    public void testInvNamedArgumentInvocationLocal(){
        compareWithJavaSource("invoke/NamedArgumentInvocationLocal");
    }

    @Test
    public void testInvSequencedParameterInvocation(){
        compareWithJavaSource("invoke/SequencedParameterInvocation");
    }

    @Test
    public void testInvToplevelMethodInvocation(){
        compareWithJavaSource("invoke/ToplevelMethodInvocation");
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
    @Test
    public void testSlfOuterReference(){
        compareWithJavaSource("selfref/OuterReference");
    }
    
    // Instantiation
    
    @Test
    public void testInsClassInstantiation(){
        compareWithJavaSource("instantiation/ClassInstantiation");
    }
    @Test
    public void testInsEntryInstantiation(){
        compareWithJavaSource("instantiation/EntryInstantiation");
    }
    @Test
    public void testInsGenericClassInstantiation(){
        compareWithJavaSource("instantiation/GenericClassInstantiation");
    }
    @Test
    public void testInsRangeInstantiation(){
        compareWithJavaSource("instantiation/RangeInstantiation");
    }
    @Test
    public void testInsSequenceInstantiation(){
        compareWithJavaSource("instantiation/SequenceInstantiation");
    }
    
    // Operators
    
    @Test
    public void testOprNumericOp(){
        compareWithJavaSource("operator/NumericOp");
    }
    @Test
    public void testOprLogicalOp(){
        compareWithJavaSource("operator/LogicalOp");
    }
    
    @Test
    public void testStpStringTemplate(){
        compareWithJavaSource("stringtemp/StringTemplate");
    }
}
